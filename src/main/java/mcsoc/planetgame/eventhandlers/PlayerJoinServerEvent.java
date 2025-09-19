package mcsoc.planetgame.eventhandlers;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.statemanagement.GameStateManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public abstract class PlayerJoinServerEvent {

    private PlayerJoinServerEvent() { /* delete */ }

    public static void RegisterEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            
            PlanetGame.LOGGER.info("{} joined the server, sending packet.", handler.getPlayer().getUuidAsString());
            sender.sendPacket(GameStateManager.getPlayerGravityStatePacket(handler.getPlayer()));
        });
    }
}
