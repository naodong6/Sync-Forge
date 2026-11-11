package net.sumik.sync.api.networking;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.sumik.sync.common.utils.client.PlayerUtil;
import net.sumik.sync.common.utils.reflect.Activator;
import net.sumik.sync.networking.SyncPackets;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Represents a packet that can be sent from a server to a client.
 */
public interface ClientPlayerPacket extends PlayerPacket {
    /**
     * Sends the packet to all players that are currently connected to the server.
     * @param server The server.
     */
    default void sendToAll(MinecraftServer server) {
        PlayerList playerList = server.getPlayerList();
        this.send(playerList.getPlayers());
    }

    /**
     * Sends the packet to the client.
     * @param player Player that should receive the packet.
     */
    default void send(ServerPlayer player) {
        SyncPackets.sendToPlayer(this, player);
    }

    /**
     * Sends the packet to all players within a certain radius of a position.
     * @param world The server world.
     * @param pos The position.
     * @param radius The radius.
     */
    default void send(ServerLevel world, BlockPos pos, double radius) {
        SyncPackets.sendToPlayersNear(this, world, pos.getX(), pos.getY(), pos.getZ(), radius);
    }

    /**
     * Sends the packet to the client.
     * @param players Players that should receive the packet.
     */
    default void send(Collection<ServerPlayer> players) {
        this.send(players.stream());
    }

    /**
     * Sends the packet to the client.
     * @param players Players that should receive the packet.
     */
    default void send(Stream<ServerPlayer> players) {
        players.forEach(this::send);
    }

    /**
     * @return true if the packet should be executed on the render thread;
     * otherwise, false.
     */
    default boolean isRenderTask() {
        return false;
    }

    /**
     * @return Identifier of the world the packet should be executed in.
     */
    @Nullable
    default ResourceLocation getTargetWorldId() {
        return null;
    }

    /**
     * This method is called after the packet is delivered to the client side.
     *
     * @param ctx The network context.
     */
    @OnlyIn(Dist.CLIENT)
    default void execute(IPayloadContext ctx) {
        Minecraft client = Minecraft.getInstance();
        ClientPacketListener handler = client.getConnection();

        if (handler == null) {
            return;
        }

        PlayerUtil.recordPlayerUpdate(this.getTargetWorldId(), (player, w, c) -> {
            if (this.isBackgroundTask()) {
                this.execute(client, player, handler, ctx);
            } else if (this.isRenderTask()) {
                RenderSystem.recordRenderCall(() -> this.execute(client, player, handler, ctx));
            } else {
                client.execute(() -> this.execute(client, player, handler, ctx));
            }
        });
    }

    /**
     * This method is called after the packet is delivered to the client side.
     *
     * @param client The client.
     * @param player The player.
     * @param handler The network handler that received this packet
     * @param ctx The network context.
     */
    @OnlyIn(Dist.CLIENT)
    default void execute(Minecraft client, LocalPlayer player, ClientPacketListener handler, IPayloadContext ctx) { }

    /**
     * Handles the packet received from the network.
     * This is called by Forge's networking system.
     *
     * @param ctx The network context.
     */
    default void handle(IPayloadContext ctx) {
        if (ctx.flow() == PacketFlow.CLIENTBOUND) {
            ctx.enqueueWork(() -> execute(ctx));
        }
    }

    /**
     * Encodes this packet to the buffer.
     *
     * @param buffer The buffer to write to.
     */
    default void encode(FriendlyByteBuf buffer) {
        this.write(buffer);
    }

    /**
     * Registers a client side handler for the specified packet.
     * Note: In Forge, registration is handled through SyncPackets.initClient()
     * This method exists for API compatibility.
     *
     * @param type Class of the packet.
     * @param <T> The packet type.
     */
    @OnlyIn(Dist.CLIENT)
    static <T extends ClientPlayerPacket> void register(Class<T> type) {
        // In Forge, registration is handled differently through SyncPackets.initClient()
        // This method exists for API compatibility but the actual registration
        // happens in SyncPackets using the Forge SimpleChannel system

        // The registration logic from Fabric would be:
        // Supplier<T> supplier = Activator.createSupplier(type).orElseThrow();
        // But in Forge we use the SimpleChannel registration in SyncPackets
    }
}
