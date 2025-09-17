package mcsoc.planetgame.eventhandlers;

import gravity_changer.api.GravityChangerAPI;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.statemanagement.GameStateManager;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PerTickServerEvent {
    private PerTickServerEvent() { /* delete */}

    private static void updateClientGravityState(ServerPlayerEntity player) {
        GravityChangerAPI.setBaseGravityDirection(player, GameStateManager.getPlayerGravityDirection(player));
        GravityChangerAPI.setBaseGravityStrength(player, GameStateManager.getPlayerGravityStrength(player).getDouble());
        player.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY).setBaseValue(LivingEntity.GRAVITY * GameStateManager.getPlayerGravityStrength(player).getDouble());
        
        ServerPlayNetworking.send(player, GameStateManager.getPlayerGravityStatePacket(player));
    }
    
    public static void Register() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {

            // if update necessary (TODO: IMPLEMENT THIS)
            world.getPlayers().stream().filter(GameStateManager::getPlayerGravityModified)
            .forEach(PerTickServerEvent::updateClientGravityState);

            MinecraftServer server = world.getServer();
            GameStateManager.forEachPlayer(
                server, 
                e -> GameEffects.tickPlayerState(e.getKey(), server)
            );
            GameStateManager.updateTickTimings(server);
        });
        
    }
}
