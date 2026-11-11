package net.sumik.sync.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.sumik.sync.Sync;
import net.sumik.sync.api.networking.ClientPlayerPacket;
import net.sumik.sync.api.networking.PlayerIsAlivePacket;
import net.sumik.sync.api.networking.ServerPlayerPacket;
import net.sumik.sync.api.networking.ShellDestroyedPacket;
import net.sumik.sync.api.networking.ShellStateUpdatePacket;
import net.sumik.sync.api.networking.ShellUpdatePacket;
import net.sumik.sync.api.networking.SynchronizationRequestPacket;
import net.sumik.sync.api.networking.SynchronizationResponsePacket;

import java.util.function.Supplier;

public final class SyncPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final CustomPacketPayload.Type<SynchronizationRequestPacket> SYNCHRONIZATION_REQUEST =
            type("synchronization_request");
    public static final CustomPacketPayload.Type<SynchronizationResponsePacket> SYNCHRONIZATION_RESPONSE =
            type("synchronization_response");
    public static final CustomPacketPayload.Type<ShellUpdatePacket> SHELL_UPDATE =
            type("shell_update");
    public static final CustomPacketPayload.Type<ShellStateUpdatePacket> SHELL_STATE_UPDATE =
            type("shell_state_update");
    public static final CustomPacketPayload.Type<PlayerIsAlivePacket> PLAYER_IS_ALIVE =
            type("player_is_alive");
    public static final CustomPacketPayload.Type<ShellDestroyedPacket> SHELL_DESTROYED =
            type("shell_destroyed");

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        registrar.playToServer(SYNCHRONIZATION_REQUEST,
                codec(SynchronizationRequestPacket::new),
                (packet, context) -> packet.handle(context));

        registrar.playToClient(SYNCHRONIZATION_RESPONSE,
                codec(SynchronizationResponsePacket::new),
                (packet, context) -> packet.handle(context));
        registrar.playToClient(SHELL_UPDATE,
                codec(ShellUpdatePacket::new),
                (packet, context) -> packet.handle(context));
        registrar.playToClient(SHELL_STATE_UPDATE,
                codec(ShellStateUpdatePacket::new),
                (packet, context) -> packet.handle(context));
        registrar.playToClient(PLAYER_IS_ALIVE,
                codec(PlayerIsAlivePacket::new),
                (packet, context) -> packet.handle(context));
        registrar.playToClient(SHELL_DESTROYED,
                codec(ShellDestroyedPacket::new),
                (packet, context) -> packet.handle(context));
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToAllPlayers(CustomPacketPayload packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }

    public static void sendToAllTracking(CustomPacketPayload packet, LevelChunk chunk) {
        PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) chunk.getLevel(), chunk.getPos(), packet);
    }

    public static void sendToPlayersNear(CustomPacketPayload packet, ServerLevel world, double x, double y, double z, double radius) {
        PacketDistributor.sendToPlayersNear(world, null, x, y, z, radius, packet);
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> type(String path) {
        return new CustomPacketPayload.Type<>(Sync.locate(path));
    }

    private static <T extends CustomPacketPayload> StreamCodec<RegistryFriendlyByteBuf, T> codec(Supplier<T> factory) {
        return CustomPacketPayload.codec(
                (packet, buffer) -> {
                    if (packet instanceof net.sumik.sync.api.networking.PlayerPacket playerPacket) {
                        playerPacket.write(buffer);
                    }
                },
                buffer -> {
                    T packet = factory.get();
                    if (packet instanceof net.sumik.sync.api.networking.PlayerPacket playerPacket) {
                        playerPacket.read(buffer);
                    }
                    return packet;
                }
        );
    }

    private SyncPackets() {
    }
}
