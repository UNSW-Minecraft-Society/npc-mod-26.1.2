package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.networking.TriggerFirstAbilityC2SPayload;
import mcsoc.planetgame.statemanagement.GameEffects;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class GravityAbilityTriggerPacketEvent {
    private GravityAbilityTriggerPacketEvent() { /* delete */}

    public static void Register() {

        ServerPlayNetworking.registerGlobalReceiver(TriggerFirstAbilityC2SPayload.ID, (payload, context) -> {
            GameEffects.triggerGravAbility(context.player());
        });
    }
}
