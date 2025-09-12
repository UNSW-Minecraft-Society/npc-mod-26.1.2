package mcsoc.planetgame.EventHandlers;

import gravity_changer.api.GravityChangerAPI;
import mcsoc.planetgame.StateManagement.GameStateManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public abstract class PerTickServerEvent {
    private PerTickServerEvent() { /* delete */}
    
    public static void Register() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            world.getPlayers().forEach(player -> {
                GravityChangerAPI.setBaseGravityDirection(player, GameStateManager.getPlayerGravityDirection(player));
                GravityChangerAPI.setBaseGravityStrength(player, GameStateManager.getPlayerGravityStrength(player));
            });
        });
        
    }
}
