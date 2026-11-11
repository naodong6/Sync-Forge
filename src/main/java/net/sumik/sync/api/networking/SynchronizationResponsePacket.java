package net.sumik.sync.api.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sumik.sync.Sync;
import net.sumik.sync.api.shell.ClientShell;
import net.sumik.sync.api.shell.ShellState;
import net.sumik.sync.networking.SyncPackets;
import org.jetbrains.annotations.Nullable;

public class SynchronizationResponsePacket implements ClientPlayerPacket {
    private ResourceLocation startWorld;
    private BlockPos startPos;
    private Direction startFacing;
    private ResourceLocation targetWorld;
    private BlockPos targetPos;
    private Direction targetFacing;
    private ShellState storedState;

    public SynchronizationResponsePacket() {
    }

    public SynchronizationResponsePacket(ResourceLocation startWorld, BlockPos startPos, Direction startFacing, ResourceLocation targetWorld, BlockPos targetPos, Direction targetFacing, @Nullable ShellState storedState) {
        this.startWorld = startWorld;
        this.startPos = startPos;
        this.startFacing = startFacing;
        this.targetWorld = targetWorld;
        this.targetPos = targetPos;
        this.targetFacing = targetFacing;
        this.storedState = storedState;
    }

    @Override
    public CustomPacketPayload.Type<SynchronizationResponsePacket> type() {
        return SyncPackets.SYNCHRONIZATION_RESPONSE;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "packet.shell.synchronization.response");
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.startWorld);
        buffer.writeBlockPos(this.startPos);
        buffer.writeVarInt(this.startFacing.get3DDataValue());
        buffer.writeResourceLocation(this.targetWorld);
        buffer.writeBlockPos(this.targetPos);
        buffer.writeVarInt(this.targetFacing.get3DDataValue());
        if (this.storedState == null) {
            buffer.writeBoolean(false);
        } else {
            buffer.writeBoolean(true);
            buffer.writeNbt(this.storedState.writeNbt(new CompoundTag()));
        }
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        this.startWorld = buffer.readResourceLocation();
        this.startPos = buffer.readBlockPos();
        this.startFacing = Direction.from3DDataValue(buffer.readVarInt());
        this.targetWorld = buffer.readResourceLocation();
        this.targetPos = buffer.readBlockPos();
        this.targetFacing = Direction.from3DDataValue(buffer.readVarInt());
        this.storedState = buffer.readBoolean() ? ShellState.fromNbt(buffer.readNbt()) : null;
    }

    @Override
    public ResourceLocation getTargetWorldId() {
        return this.targetWorld;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(Minecraft client, LocalPlayer player, ClientPacketListener handler, IPayloadContext ctx) {
        ((ClientShell)player).endSync(this.startWorld, this.startPos, this.startFacing,
                this.targetWorld, this.targetPos, this.targetFacing,
                this.storedState);
    }
}
