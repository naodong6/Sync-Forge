package net.sumik.sync.api.networking;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sumik.sync.Sync;
import net.sumik.sync.api.shell.ServerShell;
import net.sumik.sync.api.shell.ShellState;
import net.sumik.sync.api.shell.ShellStateContainer;
import net.sumik.sync.common.utils.BlockPosUtil;
import net.sumik.sync.common.utils.WorldUtil;
import net.sumik.sync.networking.SyncPackets;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SynchronizationRequestPacket implements ServerPlayerPacket {
    private UUID shellUuid;

    public SynchronizationRequestPacket() {
    }

    public SynchronizationRequestPacket(ShellState shell) {
        this.shellUuid = shell == null ? null : shell.getUuid();
    }

    public SynchronizationRequestPacket(UUID shellUuid) {
        this.shellUuid = shellUuid;
    }

    @Override
    public CustomPacketPayload.Type<SynchronizationRequestPacket> type() {
        return SyncPackets.SYNCHRONIZATION_REQUEST;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "packet.shell.synchronization.request");
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        if (this.shellUuid == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeUUID(this.shellUuid);
        }
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        this.shellUuid = buffer.readBoolean() ? buffer.readUUID() : null;
    }

    @Override
    public void execute(MinecraftServer server, ServerPlayer player, IPayloadContext ctx) {
        ServerShell shell = (ServerShell)player;
        ShellState state = shell.getShellStateByUuid(this.shellUuid);

        BlockPos currentPos = player.blockPosition();
        Level currentWorld = player.level();
        ResourceLocation currentWorldId = WorldUtil.getId(currentWorld);
        Direction currentFacing = BlockPosUtil.getHorizontalFacing(currentPos, currentWorld).orElse(player.getDirection().getOpposite());

        shell.sync(state).ifLeft(storedState -> {
            Objects.requireNonNull(state);
            ResourceLocation targetWorldId = state.getWorld();
            BlockPos targetPos = state.getPos();
            Direction targetFacing = player.getDirection().getOpposite();
            new SynchronizationResponsePacket(currentWorldId, currentPos, currentFacing, targetWorldId, targetPos, targetFacing, storedState).send(player);
        }).ifRight(failureReason -> {
            player.sendSystemMessage(failureReason.toText());
            new SynchronizationResponsePacket(currentWorldId, currentPos, currentFacing, currentWorldId, currentPos, currentFacing, null).send(player);
        });
    }}
