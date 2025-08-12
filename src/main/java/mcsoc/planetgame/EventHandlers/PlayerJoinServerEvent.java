package mcsoc.planetgame.EventHandlers;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.Networking.SyncPlayerDataS2CPayload;
import mcsoc.planetgame.StateManagement.GameState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public abstract class PlayerJoinServerEvent {

    public static void Register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            
            PlanetGame.LOGGER.info("{} joined the server, sending packet.", handler.getPlayer().getUuidAsString());

            sender.sendPacket(new SyncPlayerDataS2CPayload(
                GameState.getOrCreatePlayerState(handler.getPlayer(), server))
            );
        });
    }
}
