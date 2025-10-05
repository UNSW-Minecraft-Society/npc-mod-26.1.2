package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.networking.SyncPlayerDrillingDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public abstract class MiningSyncPacketEvent {

    private MiningSyncPacketEvent() { /* delete */}

    public static void register() {

        PayloadTypeRegistry.playS2C().register(SyncPlayerDrillingDataS2CPayload.ID, SyncPlayerDrillingDataS2CPayload.CODEC);    

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerDrillingDataS2CPayload.ID, (payload, context) -> {
            PlanetGameClient.updateXrayState(payload);
        });
    }
}
