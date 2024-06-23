package net.extendeddrawersaddon.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DrawerData(int slotCount) implements CustomPayload {

    public static final CustomPayload.Id<DrawerData> PACKET_ID = new CustomPayload.Id<>(Identifier.of("extendeddrawersaddon", "drawer_data_packet"));

    public static final PacketCodec<RegistryByteBuf, DrawerData> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.slotCount);
    }, buf -> new DrawerData(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
