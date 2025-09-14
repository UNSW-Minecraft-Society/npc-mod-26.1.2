package mcsoc.planetgame.networking;

import net.minecraft.network.packet.CustomPayload;

public record TriggerFirstAbilityC2SPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriggerFirstAbilityC2SPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.ABILITY_1_PACKET_ID);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}