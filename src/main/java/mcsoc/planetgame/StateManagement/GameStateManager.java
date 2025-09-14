package mcsoc.planetgame.statemanagement;

import java.util.UUID;

import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import mcsoc.planetgame.statemanagement.PlayerState.GravityStrength;
import mcsoc.planetgame.statemanagement.PlayerState.PlayerAbilities1;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

public abstract class GameStateManager {
    private GameStateManager() { /* delete */ }

    public static SyncPlayerGravityDataS2CPayload getPlayerGravityStatePacket(ServerPlayerEntity player) {
        PlayerState state = GameState.getPlayerState(player);
        return new SyncPlayerGravityDataS2CPayload(
            state.grav_dir(),
            state.grav_mod()
        );
    }

    public static void flipPlayerGravity(UUID uuid, MinecraftServer server) {
        Direction new_grav = switch (GameState.getPlayerGravityDirection(uuid, server)) {
            case DOWN -> Direction.UP;
            case EAST -> Direction.WEST;
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case UP -> Direction.DOWN;
            case WEST -> Direction.EAST;
        };
        GameState.setPlayerGravityDirection(uuid, server, new_grav);
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
    }

    public static void setPlayerGravityDirection(ServerPlayerEntity player, Direction grav_dir) {
        setPlayerGravityDirection(player.getUuid(), player.getServer(), grav_dir);
    }


    public static Double getPlayerGravityStrength(UUID uuid, MinecraftServer server) {
        return GameState.getPlayerGravStrengthModifier(uuid, server);
    }

    public static Double getPlayerGravityStrength(ServerPlayerEntity player) {
        return getPlayerGravityStrength(player.getUuid(), player.getServer());
    }


    public static void setPlayerGravityStrength(UUID uuid, MinecraftServer server, Double grav_dir) {
        GameState.setPlayerGravStrengthModifier(uuid, server, grav_dir);
    }

    public static void setPlayerGravityStrength(ServerPlayerEntity player, Double grav_dir) {
        setPlayerGravityStrength(player.getUuid(), player.getServer(), grav_dir);
    }
}
