package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.gameeffects.GameEffects;
import mcsoc.planetgame.networking.TriggerThirdAbilityC2SPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ThirdAbilityTriggerPacketEvent {
    private ThirdAbilityTriggerPacketEvent() { /* delete */}

    public static void registerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(TriggerThirdAbilityC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            GameEffects.triggerThirdAbility(player);
        });
    }
}
