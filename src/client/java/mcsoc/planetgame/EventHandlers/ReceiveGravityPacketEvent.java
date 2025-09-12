package mcsoc.planetgame.EventHandlers;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.Networking.SyncPlayerGravityDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public abstract class ReceiveGravityPacketEvent {

    private ReceiveGravityPacketEvent() { /* delete */}

    public static void Register() {

        PayloadTypeRegistry.playS2C().register(SyncPlayerGravityDataS2CPayload.ID, SyncPlayerGravityDataS2CPayload.CODEC);    

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerGravityDataS2CPayload.ID, (payload, context) -> {
            PlanetGameClient.updateGravityState(payload);
        });
    }
}
