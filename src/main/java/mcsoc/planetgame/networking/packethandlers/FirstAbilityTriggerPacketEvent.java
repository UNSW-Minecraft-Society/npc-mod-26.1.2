package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.networking.TriggerFirstAbilityC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

public class FirstAbilityTriggerPacketEvent {
    private FirstAbilityTriggerPacketEvent() { /* delete */}

    public static void Register() {

        PayloadTypeRegistry.playC2S().register(TriggerFirstAbilityC2SPayload.ID, TriggerFirstAbilityC2SPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TriggerFirstAbilityC2SPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            GameEffects.triggerFirstAbility(player);
            player.networkHandler.sendPacket(new OverlayMessageS2CPacket(GameEffects.getFirstAbilityActionbarResponse(player)));
        });
    }
}
