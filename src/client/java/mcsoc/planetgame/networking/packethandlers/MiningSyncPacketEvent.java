package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.networking.SyncPlayerDrillingDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public abstract class MiningSyncPacketEvent {

    private MiningSyncPacketEvent() { /* delete */}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerDrillingDataS2CPayload.ID, (payload, context) -> {
            PlanetGameClient.updateXrayState(payload);
        });
    }
}
