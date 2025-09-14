package mcsoc.planetgame.networking;

import net.minecraft.network.packet.CustomPayload;

public record TriggerAbility1C2SPayload() implements CustomPayload {

    public static final CustomPayload.Id<TriggerAbility1C2SPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.ABILITY_1_PACKET_ID);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}