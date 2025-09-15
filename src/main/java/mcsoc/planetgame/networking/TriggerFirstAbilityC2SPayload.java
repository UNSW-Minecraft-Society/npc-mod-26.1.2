package mcsoc.planetgame.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record TriggerFirstAbilityC2SPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriggerFirstAbilityC2SPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.FIRST_ABILITY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, TriggerFirstAbilityC2SPayload> CODEC = PacketCodec.unit(new TriggerFirstAbilityC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}