package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.gameeffects.FirstAbilityGameEffects;
import mcsoc.planetgame.networking.TriggerFirstAbilityC2SPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class FirstAbilityTriggerPacketEvent {
    private FirstAbilityTriggerPacketEvent() { /* delete */}

    public static void registerHandler() {        
        ServerPlayNetworking.registerGlobalReceiver(TriggerFirstAbilityC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            FirstAbilityGameEffects.triggerFirstAbility(player);
        });
    }
}
