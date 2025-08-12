package mcsoc.planetgame.StateManagement;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.Networking.SyncPlayerDataS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class GameEffects {

    public static void toggleIsPlayerFlipped(ServerPlayerEntity player, MinecraftServer server) {
        GameState.toggleIsPlayerFlipped(player, server);

        PlanetGame.LOGGER.info("GE: toggled flip");

        ServerPlayNetworking.send(player, new SyncPlayerDataS2CPayload(
            GameState.getPlayerState(player, server)
        ));

        PlanetGame.LOGGER.info("GE: sent packet");
    }
}
