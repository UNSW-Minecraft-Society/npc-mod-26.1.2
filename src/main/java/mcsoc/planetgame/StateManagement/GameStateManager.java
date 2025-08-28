package mcsoc.planetgame.StateManagement;

import mcsoc.planetgame.Networking.SyncPlayerDataS2CPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class GameStateManager {

    private GameStateManager() { /* delete */ }

    public static SyncPlayerDataS2CPayload getPlayerStatePacket(ServerPlayerEntity player, MinecraftServer server) {
        return new SyncPlayerDataS2CPayload(
            GameState.getPlayerState(player, server)
        );
    }
}
