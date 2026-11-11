package net.sumik.sync.api.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sumik.sync.Sync;
import net.sumik.sync.api.shell.Shell;
import net.sumik.sync.api.shell.ShellState;
import net.sumik.sync.networking.SyncPackets;

import java.util.Collection;
import java.util.List;

public class ShellUpdatePacket implements ClientPlayerPacket {
    private ResourceLocation worldId;
    private boolean isArtificial;
    private Collection<ShellState> states;

    public ShellUpdatePacket() {
        this.states = List.of();
    }

    public ShellUpdatePacket(ResourceLocation worldId, boolean isArtificial, Collection<ShellState> states) {
        this.worldId = worldId;
        this.isArtificial = isArtificial;
        this.states = states == null ? List.of() : states;
    }

    @Override
    public CustomPacketPayload.Type<ShellUpdatePacket> type() {
        return SyncPackets.SHELL_UPDATE;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "packet.shell.update");
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.worldId);
        buffer.writeBoolean(this.isArtificial);
        buffer.writeVarInt(this.states.size());
        this.states.forEach(x -> buffer.writeNbt(x.writeNbt(new CompoundTag())));
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        this.worldId = buffer.readResourceLocation();
        this.isArtificial = buffer.readBoolean();
        this.states = buffer.readList(subBuffer -> ShellState.fromNbt(subBuffer.readNbt()));
    }

    @Override
    public ResourceLocation getTargetWorldId() {
        return this.worldId;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(Minecraft client, LocalPlayer player, ClientPacketListener handler, IPayloadContext ctx) {
        Shell shell = (Shell)player;
        shell.changeArtificialStatus(this.isArtificial);
        shell.setAvailableShellStates(this.states.stream());
    }
}
