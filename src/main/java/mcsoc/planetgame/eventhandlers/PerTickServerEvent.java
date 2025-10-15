package mcsoc.planetgame.eventhandlers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import gravity_changer.api.GravityChangerAPI;
import mcsoc.planetgame.blocks.gravityfieldblock.GravityFieldBlockEntity;
import mcsoc.planetgame.entities.throwables.ThrowableRockEntity;
import mcsoc.planetgame.gameeffects.CommonGameEffects;
import mcsoc.planetgame.gameeffects.FirstAbilityGameEffects;
import mcsoc.planetgame.networking.NetworkingIdentifiers;
import mcsoc.planetgame.statemanagement.GameStateManager;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class PerTickServerEvent {
    private PerTickServerEvent() { /* delete */}

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
        World world = player.getWorld();

        Set<BlockPos> relevant_grav_blocks = new HashSet<>();
        GameStateManager.forEachGravityGenerator(world.getServer(), gen_pos -> {
            Optional<BlockEntity> block_entity_optional = Optional.ofNullable(world.getBlockEntity(gen_pos));
            if (block_entity_optional.isPresent() && 
                    block_entity_optional.get() instanceof GravityFieldBlockEntity) {
                relevant_grav_blocks.add(gen_pos);
            }
        });
        
        Optional<BlockPos> closest_gen_optional = relevant_grav_blocks.stream()
        .sorted((pos1, pos2) -> pos1.getSquaredDistance(player_pos) > (pos2.getSquaredDistance(player_pos)) ? 1 : -1)
        .findFirst();

        if (closest_gen_optional.isEmpty()) {
            FirstAbilityGameEffects.setPlayerInGravityField(player, false);
        } else {
            ((GravityFieldBlockEntity)(world.getBlockEntity(closest_gen_optional.get()))).addTrackedPlayer(player);
        }
    }

    private static void updateGravityAction(ServerPlayerEntity player, boolean should_update_gravity_fields) {
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
                CommonGameEffects.tick(e.getKey(), server)
        );
    }
    
    public static void registerEvent() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {

            List<ServerPlayerEntity> player_list = server.getPlayerManager().getPlayerList();
            
            player_list.forEach(player -> {
                if (!ServerPlayNetworking.canSend(player, NetworkingIdentifiers.PLAYER_SYNC_GRAVITY_PACKET_ID)) return;

                updateGravityAction(player, GameStateManager.shouldUpdateGravityFields(server));
                if (player.isSneaking()) {
                    CommonGameEffects.dropPassengerIntentionally(player);
                }

                ServerPlayNetworking.send(player, GameStateManager.getPlayerMiningStatePacket(player));
            });
            
            updateTickTimings(server);
        });


        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (GameStateManager.getIfPlayerIsCarrying(player)) {
                GameStateManager.setIfPlayerIsCarrying(player, false);
                CommonGameEffects.attemptReturnPlayerInventory(player);
            }
            player.getWorld().getEntitiesByClass(ThrowableRockEntity.class, player.getBoundingBox().expand(1), rock -> true).forEach(rock -> rock.doDeathEffect());
        });
    }
}
