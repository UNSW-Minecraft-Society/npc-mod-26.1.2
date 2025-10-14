package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.gameeffects.SecondAbilityGameEffects;
import mcsoc.planetgame.networking.TriggerSecondAbilityC2SPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SecondAbilityTriggerPacketEvent {
    private SecondAbilityTriggerPacketEvent() { /* delete */}

    public static void registerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(TriggerSecondAbilityC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            SecondAbilityGameEffects.triggerSecondAbility(player);
        });
    }
}
