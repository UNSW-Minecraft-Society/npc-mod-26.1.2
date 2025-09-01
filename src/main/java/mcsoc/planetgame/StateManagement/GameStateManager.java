package mcsoc.planetgame.StateManagement;

import java.util.UUID;

import mcsoc.planetgame.Networking.SyncPlayerDataS2CPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

public abstract class GameStateManager {

    private GameStateManager() { /* delete */ }

    public static SyncPlayerDataS2CPayload getPlayerStatePacket(ServerPlayerEntity player, MinecraftServer server) {
        return new SyncPlayerDataS2CPayload(
            GameState.getPlayerState(player, server)
        );
    }

    protected static void flipPlayerGravity(UUID uuid, MinecraftServer server) {
        GameState.setPlayerGravityDirection(uuid, server, 
            switch (GameState.getPlayerGravityDirection(uuid, server)) {
                case DOWN -> Direction.UP;
                case EAST -> Direction.WEST;
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case UP -> Direction.DOWN;
                case WEST -> Direction.EAST;
            }
        );
    }

    protected static void flipPlayerGravity(ServerPlayerEntity player, MinecraftServer server) {
        flipPlayerGravity(player.getUuid(), server);
    }
}
