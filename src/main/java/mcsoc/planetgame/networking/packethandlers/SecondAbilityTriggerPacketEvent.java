package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.networking.TriggerFirstAbilityC2SPayload;
import mcsoc.planetgame.networking.TriggerSecondAbilityC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SecondAbilityTriggerPacketEvent {
    private SecondAbilityTriggerPacketEvent() { /* delete */}

    public static void registerHandler() {

        PayloadTypeRegistry.playC2S().register(TriggerSecondAbilityC2SPayload.ID, TriggerSecondAbilityC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TriggerSecondAbilityC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            GameEffects.triggerSecondAbility(player);
        });
    }
}
