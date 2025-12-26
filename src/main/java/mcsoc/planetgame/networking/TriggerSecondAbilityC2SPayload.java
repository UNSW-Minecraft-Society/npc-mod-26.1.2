package mcsoc.planetgame.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record TriggerSecondAbilityC2SPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriggerSecondAbilityC2SPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.TRIGGER_SECOND_ABILITY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, TriggerSecondAbilityC2SPayload> CODEC = PacketCodec.unit(new TriggerSecondAbilityC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}