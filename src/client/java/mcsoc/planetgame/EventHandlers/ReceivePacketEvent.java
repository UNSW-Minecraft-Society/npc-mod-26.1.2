package mcsoc.planetgame.EventHandlers;

import mcsoc.planetgame.PlanetGameClient;
import mcsoc.planetgame.Networking.SyncPlayerDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public abstract class ReceivePacketEvent {

    public static void Register() {

        PayloadTypeRegistry.playS2C().register(SyncPlayerDataS2CPayload.ID, SyncPlayerDataS2CPayload.CODEC);    

        ClientPlayNetworking.registerGlobalReceiver(SyncPlayerDataS2CPayload.ID, (payload, context) -> {

            PlanetGameClient.LOGGER.info("recieved sync packet");
            PlanetGameClient.setPlayerState(payload.state());
        });
    }
}
