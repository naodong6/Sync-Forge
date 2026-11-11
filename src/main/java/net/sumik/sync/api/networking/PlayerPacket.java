package net.sumik.sync.api.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a packet that can be used for communication between a client and a server.
 */
public interface PlayerPacket extends CustomPacketPayload {
    /**
     * @return Identifier of the packet.
     */
    ResourceLocation getId();

    /**
     * Writes packet data to the buffer.
     * @param buffer The buffer.
     */
    void write(FriendlyByteBuf buffer);

    /**
     * Reads packet data from the buffer.
     * @param buffer The buffer.
     */
    void read(FriendlyByteBuf buffer);

    /**
     * @return true if the packet can be executed in background;
     * otherwise, false.
     */
    default boolean isBackgroundTask() {
        return false;
    }
}
