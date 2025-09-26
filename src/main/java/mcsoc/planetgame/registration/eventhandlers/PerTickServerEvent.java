package mcsoc.planetgame.registration.eventhandlers;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;

import gravity_changer.api.GravityChangerAPI;

import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.registration.blocks.gravityfieldblock.GravityFieldBlockEntity;
import mcsoc.planetgame.statemanagement.GameStateManager;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public abstract class PerTickServerEvent {
    private PerTickServerEvent() { /* delete */}

    private static final int CHUNK_RADIUS = 1;
    
    public static final int GRAV_FIELD_UPDATE_TIME_TICKS = 2;

    private static void updateClientGravityState(ServerPlayerEntity player) {
        GravityChangerAPI.setBaseGravityDirection(player, GameStateManager.getPlayerGravityDirection(player));
        GravityChangerAPI.setBaseGravityStrength(player, GameStateManager.getPlayerGravityStrength(player).getDouble());
        player.getAttributeInstance(EntityAttributes.GENERIC_GRAVITY).setBaseValue(LivingEntity.GRAVITY * GameStateManager.getPlayerGravityStrength(player).getDouble());
        ServerPlayNetworking.send(player, GameStateManager.getPlayerGravityStatePacket(player));
    }


    private static void updateClosestGravityGenerator(ServerPlayerEntity player) {
        // register closest grav field block
        Vec3d player_pos = player.getPos();
        int player_chunk_x = player.getChunkPos().x;
        int player_chunk_z = player.getChunkPos().z;

        Set<Entry<BlockPos,BlockEntity>> relevant_grav_blocks = new HashSet<>();
        for (int i = -CHUNK_RADIUS; i <= CHUNK_RADIUS; i++) {
            for (int j = -CHUNK_RADIUS; j <= CHUNK_RADIUS; j++) {
                player.getWorld().getChunk(player_chunk_x + i, player_chunk_z + j).getBlockEntities().entrySet().stream()
                .filter(e -> e.getValue() instanceof GravityFieldBlockEntity).forEach(e -> relevant_grav_blocks.add(e));
            }
        }
        
        Entry<BlockPos,BlockEntity> closest_gen = relevant_grav_blocks.stream()
        .sorted((e1, e2) -> e1.getKey().getSquaredDistance(player_pos) > (e2.getKey().getSquaredDistance(player_pos)) ? 1 : -1)
        .findFirst().orElse(null);

        if (Objects.nonNull(closest_gen)) {
            ((GravityFieldBlockEntity)closest_gen.getValue()).addTrackedPlayer(player);
        } else {
            GameEffects.setPlayerInGravityField(player, false);
        }
    };


    private static void updateGravityEffects(ServerPlayerEntity player, boolean should_update_gravity_fields) {
        if (should_update_gravity_fields) {
            updateClosestGravityGenerator(player);
        }
        if (!GameStateManager.getPlayerInGravityField(player) || GameStateManager.getPlayerGravityModified(player)) {
            updateClientGravityState(player);
        }
    }


    private static void updateTickTimings(MinecraftServer server) {
        GameStateManager.updateTickTimings(server);
        GameStateManager.tickGravityFieldTimer(server);
        GameStateManager.forEachPlayerEntry(server, e -> 
                GameEffects.tickPlayerState(e.getKey(), server)
        );
    }
    
    public static void registerEvent() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            MinecraftServer server = world.getServer();
            List<ServerPlayerEntity> player_list = server.getPlayerManager().getPlayerList();
            
            player_list.forEach(player -> {
                updateGravityEffects(player, GameStateManager.shouldUpdateGravityFields(server));
                if (player.isSneaking()) {
                    GameEffects.dropPassengerIntentionally(player);
                }
            });
            
            updateTickTimings(server);
        });
    }
}
