package mcsoc.planetgame.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record TriggerThirdAbilityC2SPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriggerThirdAbilityC2SPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.THIRD_ABILITY_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, TriggerThirdAbilityC2SPayload> CODEC = PacketCodec.unit(new TriggerThirdAbilityC2SPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}