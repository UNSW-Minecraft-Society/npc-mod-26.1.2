package mcsoc.planetgame.networking.packethandlers;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public abstract class GravitySyncPacketEvent {

    private GravitySyncPacketEvent() { /* delete */}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerGravityDataS2CPayload.ID, (payload, context) -> {
            PlanetGameClient.updateGravityState(payload);
        });
    }
}
