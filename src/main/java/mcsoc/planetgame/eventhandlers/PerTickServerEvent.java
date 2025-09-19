package mcsoc.planetgame.eventhandlers;

import gravity_changer.GravityChangerMod;
import gravity_changer.api.GravityChangerAPI;
import gravity_changer.mixin.EntityMixin;
import gravity_changer.mixin.client.CameraMixin;
import gravity_changer.util.RotationUtil;
import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.PlanetGame;
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
    
    public static void RegisterEvent() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            world.getPlayers().forEach(player -> {
                if (GameStateManager.getPlayerGravityModified(player)) {
                    PerTickServerEvent.updateClientGravityState(player);
                }
            });

            MinecraftServer server = world.getServer();
            GameStateManager.forEachPlayer(server, 
                e -> GameEffects.tickPlayerState(e.getKey(), server)
            );
            
            GameStateManager.updateTickTimings(server);
        });
        
    }
}
