package net.sumik.sync.api.networking;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sumik.sync.Sync;
import net.sumik.sync.networking.SyncPackets;

import java.util.UUID;

public class PlayerIsAlivePacket implements ClientPlayerPacket {
    private UUID playerUuid;

    public PlayerIsAlivePacket() {
        this.playerUuid = Util.NIL_UUID;
    }

    public PlayerIsAlivePacket(Player player) {
        this(player == null ? null : player.getUUID());
    }

    public PlayerIsAlivePacket(UUID playerUuid) {
        this.playerUuid = playerUuid == null ? Util.NIL_UUID : playerUuid;
    }

    @Override
    public CustomPacketPayload.Type<PlayerIsAlivePacket> type() {
        return SyncPackets.PLAYER_IS_ALIVE;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "packet.shell.alive");
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.playerUuid);
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        this.playerUuid = buffer.readUUID();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void execute(Minecraft client, LocalPlayer player, ClientPacketListener handler, IPayloadContext ctx) {
        Player updatedPlayer = player.clientLevel.getPlayerByUUID(this.playerUuid);
        if (updatedPlayer != null) {
            if (updatedPlayer.getHealth() <= 0) {
                updatedPlayer.setHealth(0.01F);
            }
            updatedPlayer.deathTime = 0;
        }
    }
}
