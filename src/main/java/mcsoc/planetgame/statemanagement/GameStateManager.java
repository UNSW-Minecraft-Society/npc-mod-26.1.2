package mcsoc.planetgame.statemanagement;

import java.time.Instant;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import mcsoc.planetgame.networking.SyncPlayerDrillingDataS2CPayload;
import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import mcsoc.planetgame.registration.blocks.gravityfieldblock.GravityFieldBlockEntity;
import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import mcsoc.planetgame.statemanagement.playerstate.ManagedPlayerState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class GameStateManager {
    private GameStateManager() { /* delete */ }

    public static SyncPlayerGravityDataS2CPayload getPlayerGravityStatePacket(ServerPlayerEntity player) {
        ManagedPlayerState state = GameState.getPlayerState(player);
        return new SyncPlayerGravityDataS2CPayload(
            state.getCurrentPlayerGravityDirection(),
            state.getPlayerGravityStrengthModifier()
        );
    }

    public static SyncPlayerDrillingDataS2CPayload getPlayerMiningStatePacket(ServerPlayerEntity player) {
        ManagedPlayerState state = GameState.getPlayerState(player);
        return new SyncPlayerDrillingDataS2CPayload(
            state.getPlayerSecondAbility().equals(PlayerSecondAbilities.XRAY) && state.getPlayerSecondAbilityState()
        );
    }

    public static void flipPlayerGravity(UUID uuid, MinecraftServer server) {
        Direction new_grav = switch (getPlayerGravityDirection(uuid, server)) {
            case DOWN -> Direction.UP;
            case EAST -> Direction.WEST;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case UP -> Direction.DOWN;
            case WEST -> Direction.EAST;
        };
        setPlayerGravityDirection(uuid, server, new_grav);
    }

    public static void flipPlayerGravity(ServerPlayerEntity player) {
        flipPlayerGravity(player.getUuid(), player.getServer());
    }


    public static Direction getPlayerGravityDirection(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerGravityDirection(uuid, server);
    }

    public static Direction getPlayerGravityDirection(ServerPlayerEntity player) {
        return getPlayerGravityDirection(player.getUuid(), player.getServer());
    }


    public static void setPlayerGravityDirection(UUID uuid, MinecraftServer server, Direction grav_dir) {
        GameState.setPlayerGravityDirection(uuid, server, grav_dir);
        setPlayerGravityModified(uuid, server);
    }

    public static void setPlayerGravityDirection(ServerPlayerEntity player, Direction grav_dir) {
        setPlayerGravityDirection(player.getUuid(), player.getServer(), grav_dir);
    }


    public static GravityStrength getPlayerGravityStrength(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerGravityStrengthModifier(uuid, server);
    }

    public static GravityStrength getPlayerGravityStrength(ServerPlayerEntity player) {
        return getPlayerGravityStrength(player.getUuid(), player.getServer());
    }


    public static void setPlayerGravityStrength(UUID uuid, MinecraftServer server, GravityStrength grav_strength) {
        GameState.setPlayerGravityStrengthModifier(uuid, server, grav_strength);
        setPlayerGravityModified(uuid, server);
    }

    public static void setPlayerGravityStrength(ServerPlayerEntity player, GravityStrength grav_strength) {
        setPlayerGravityStrength(player.getUuid(), player.getServer(), grav_strength);
    }

    
    public static PlayerFirstAbilities getPlayerFirstAbility(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerFirstAbility(uuid, server);
    }

    public static PlayerFirstAbilities getPlayerFirstAbility(ServerPlayerEntity player) {
        return getPlayerFirstAbility(player.getUuid(), player.getServer());
    }

    public static void setPlayerFirstAbility(UUID uuid, MinecraftServer server, PlayerFirstAbilities ability) {
        GameState.setPlayerFirstAbility(uuid, server, ability);
        setPlayerGravityDirection(uuid, server, Direction.DOWN);
        setPlayerGravityStrength(uuid, server, GravityStrength.getDefault());
    }

    public static void setPlayerFirstAbility(ServerPlayerEntity player, PlayerFirstAbilities ability) {
        setPlayerFirstAbility(player.getUuid(), player.getServer(), ability);
    }

    public static boolean getPlayerGravityModified(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerGravityModified(uuid, server);
    }

    public static boolean getPlayerGravityModified(ServerPlayerEntity player) {
        return getPlayerGravityModified(player.getUuid(), player.getServer());
    }

    public static void setPlayerGravityModified(UUID uuid, MinecraftServer server) {
        GameState.setPlayerGravityModified(uuid, server);
    }

    public static void setPlayerGravityModified(ServerPlayerEntity player) {
        setPlayerGravityModified(player.getUuid(), player.getServer());
    }

    public static boolean getPlayerInGravityField(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerInGravityField(uuid, server);
    }

    public static boolean getPlayerInGravityField(ServerPlayerEntity player) {
        return getPlayerInGravityField(player.getUuid(), player.getServer());
    }

    public static void setPlayerInGravityField(UUID uuid, MinecraftServer server, boolean in_field) {
        GameState.setPlayerInGravityField(uuid, server, in_field);
    }

    public static void setPlayerInGravityField(ServerPlayerEntity player, boolean in_field) {
        setPlayerInGravityField(player.getUuid(), player.getServer(), in_field);
    }


    public static PlayerSecondAbilities getPlayerSecondAbility(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerSecondAbility(uuid, server);
    }

    public static PlayerSecondAbilities getPlayerSecondAbility(ServerPlayerEntity player) {
        return getPlayerSecondAbility(player.getUuid(), player.getServer());
    }

    public static void setPlayerSecondAbility(UUID uuid, MinecraftServer server, PlayerSecondAbilities ability) {
        GameState.setPlayerSecondAbility(uuid, server, ability);
        // TODO reset stuff here
    }

    public static void setPlayerSecondAbility(ServerPlayerEntity player, PlayerSecondAbilities ability) {
        setPlayerSecondAbility(player.getUuid(), player.getServer(), ability);
    }

    public static boolean getPlayerSecondAbilityState(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerSecondAbilityState(uuid, server);
    }

    public static boolean getPlayerSecondAbilityState(ServerPlayerEntity player) {
        return getPlayerSecondAbilityState(player.getUuid(), player.getServer());
    }

    public static void setPlayerSecondAbilityState(UUID uuid, MinecraftServer server, boolean second_ability_active) {
        GameState.setPlayerSecondAbilityState(uuid, server, second_ability_active);
        // TODO reset stuff here
    }

    public static void setPlayerSecondAbilityState(ServerPlayerEntity player, boolean second_ability_active) {
        setPlayerSecondAbilityState(player.getUuid(), player.getServer(), second_ability_active);
    }

    public static void togglePlayerSecondAbilityState(UUID uuid, MinecraftServer server) {
        GameState.setPlayerSecondAbilityState(uuid, server, !GameState.getPlayerSecondAbilityState(uuid, server));
        // TODO reset stuff here
    }

    public static void togglePlayerSecondAbilityState(ServerPlayerEntity player) {
        togglePlayerSecondAbilityState(player.getUuid(), player.getServer());
    }

    public static double getPlayerDrillCharge(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerDrillCharge(uuid, server);
    }

    public static double getPlayerDrillCharge(ServerPlayerEntity player) {
        return getPlayerDrillCharge(player.getUuid(), player.getServer());
    }

    public static void setPlayerDrillCharge(UUID uuid, MinecraftServer server, double new_drill_charge) {
        GameState.setPlayerDrillCharge(uuid, server, new_drill_charge);
    }

    public static void setPlayerDrillCharge(ServerPlayerEntity player, double new_drill_charge) {
        setPlayerDrillCharge(player.getUuid(), player.getServer(), new_drill_charge);
    }

    public static double getPlayerDrillHeat(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerDrillHeat(uuid, server);
    }

    public static double getPlayerDrillHeat(ServerPlayerEntity player) {
        return getPlayerDrillHeat(player.getUuid(), player.getServer());
    }

    public static void setPlayerDrillHeat(UUID uuid, MinecraftServer server, double new_drill_heat) {
        GameState.setPlayerDrillHeat(uuid, server, new_drill_heat);
    }

    public static void setPlayerDrillHeat(ServerPlayerEntity player, double new_drill_heat) {
        setPlayerDrillHeat(player.getUuid(), player.getServer(), new_drill_heat);
    }

    public static void incrementPlayerDrillHeat(UUID uuid, MinecraftServer server, double added_drill_heat) {
        GameState.incrementPlayerDrillHeat(uuid, server, added_drill_heat);
    }

    public static void incrementPlayerDrillHeat(ServerPlayerEntity player, double added_drill_heat) {
        incrementPlayerDrillHeat(player.getUuid(), player.getServer(), added_drill_heat);
    }

    public static void decrementPlayerDrillHeat(UUID uuid, MinecraftServer server, double removed_drill_heat) {
        GameState.decrementPlayerDrillHeat(uuid, server, removed_drill_heat);
    }

    public static void decrementPlayerDrillHeat(ServerPlayerEntity player, double removed_drill_heat) {
        decrementPlayerDrillHeat(player.getUuid(), player.getServer(), removed_drill_heat);
    }


    public static PlayerThirdAbilities getPlayerThirdAbility(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerThirdAbility(uuid, server);
    }

    public static PlayerThirdAbilities getPlayerThirdAbility(ServerPlayerEntity player) {
        return getPlayerThirdAbility(player.getUuid(), player.getServer());
    }

    public static void setPlayerThirdAbility(UUID uuid, MinecraftServer server, PlayerThirdAbilities ability) {
        GameState.setPlayerThirdAbility(uuid, server, ability);
        // TODO reset stuff here
        setIfPlayerIsCarrying(uuid, server, false);
    }

    public static void setPlayerThirdAbility(ServerPlayerEntity player, PlayerThirdAbilities ability) {
        setPlayerThirdAbility(player.getUuid(), player.getServer(), ability);
    }

    public static int getPlayerThirdAbilityCooldownTicks(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerThirdAbilityCooldownTicks(uuid, server);
    }

    public static int getPlayerThirdAbilityCooldownTicks(ServerPlayerEntity player) {
        return getPlayerThirdAbilityCooldownTicks(player.getUuid(), player.getServer());
    }

    public static void setPlayerThirdAbilityCooldownTicks(UUID uuid, MinecraftServer server, int ticks) {
        GameState.setPlayerThirdAbilityCooldownTicks(uuid, server, ticks);
    }

    public static void setPlayerThirdAbilityCooldownTicks(ServerPlayerEntity player, int ticks) {
        setPlayerThirdAbilityCooldownTicks(player.getUuid(), player.getServer(), ticks);
    }

    public static boolean getIfPlayerIsCarrying(UUID uuid, MinecraftServer server) {
        return GameState.getIfPlayerIsCarrying(uuid, server);
    }

    public static boolean getIfPlayerIsCarrying(ServerPlayerEntity player) {
        return getIfPlayerIsCarrying(player.getUuid(), player.getServer());
    }

    public static void setIfPlayerIsCarrying(UUID uuid, MinecraftServer server, boolean is_carrying) {
        GameState.setIfPlayerIsCarrying(uuid, server, is_carrying);
    }

    public static void setIfPlayerIsCarrying(ServerPlayerEntity player, boolean is_carrying) {
        setIfPlayerIsCarrying(player.getUuid(), player.getServer(), is_carrying);
    }


    public static void tickPlayerState(UUID uuid, MinecraftServer server) {

        GameState.tickPlayerState(uuid, server);
    }

    public static void tickPlayerState(ServerPlayerEntity player) {
        GameState.tickPlayerState(player.getUuid(), player.getServer());
    }

    public static void tickGravityFieldTimer(MinecraftServer server) {
        if (shouldUpdateGravityFields(server)) {
            resetGravityFieldTimer(server);
        }
        GameState.tickGravityFieldTimer(server);
    }

    public static void resetGravityFieldTimer(MinecraftServer server) {
        GameState.resetGravityFieldTimer(server);
    }

    public static boolean shouldUpdateGravityFields(MinecraftServer server) {
        return GameState.shouldUpdateGravityFields(server);
    }


    public static void forEachPlayerEntry(MinecraftServer server, Consumer<Entry<UUID, ManagedPlayerState>> action) {
        GameState state = GameState.getServerState(server);
        state.getPlayerEntryStream().forEach(action);
    }


    public static Instant getTimer(MinecraftServer server) {
        return GameState.getTimer(server);
    }

    public static void updateTickTimings(MinecraftServer server) {
        GameState.updateTickTimings(server);
    }


    public static void registerGravityGeneratorPosition(MinecraftServer server, GravityFieldBlockEntity entity) {
        GameState.registerGravityGeneratorPosition(server, entity);
    }

    public static void forEachGravityGenerator(MinecraftServer server, Consumer<BlockPos> todo_for_each) {
        GameState.forEachGravityGenerator(server, todo_for_each);
    }

    public static void addInventoryToHeap(UUID uuid, MinecraftServer server, PlayerInventory inventory) {
        GameState.addInventoryToHeap(uuid, server, inventory);
    }

    public static void addInventoryToHeap(ServerPlayerEntity player, PlayerInventory inventory) {
        addInventoryToHeap(player.getUuid(), player.getServer(), inventory);
    }


    public static Optional<Inventory> retrieveOptionalInventoryFromHeap(UUID uuid, MinecraftServer server) {
        return GameState.retrieveInventoryFromHeap(uuid, server);
    }

    public static Optional<Inventory> retrieveOptionalInventoryFromHeap(ServerPlayerEntity player) {
        return retrieveOptionalInventoryFromHeap(player.getUuid(), player.getServer());
    }

    public static Inventory retrieveInventoryFromHeap(UUID uuid, MinecraftServer server) {
        return retrieveOptionalInventoryFromHeap(uuid, server).orElseThrow();
    }

    public static Inventory retrieveInventoryFromHeap(ServerPlayerEntity player) {
        return retrieveInventoryFromHeap(player.getUuid(), player.getServer());
    }

}
