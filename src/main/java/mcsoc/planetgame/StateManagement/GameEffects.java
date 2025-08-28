package mcsoc.planetgame.StateManagement;

import mcsoc.planetgame.PlanetGame;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class GameEffects {
    private GameEffects() { /* delete */ }

    public static class CommandActions {
        private CommandActions() { /* delete */ }

        public static int flipCallingPlayerCommand(CommandContext<ServerCommandSource> cxt) {

            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameEffects.toggleIsPlayerFlipped(player);

            return 1;
        }
    }


    public static void toggleIsPlayerFlipped(ServerPlayerEntity player) {

        PlanetGame.LOGGER.info("GE: toggled flip");

        ServerPlayNetworking.send(player, new SyncPlayerDataS2CPayload(
            GameState.getPlayerState(player, server)
        ));

        PlanetGame.LOGGER.info("GE: sent packet");
    }
}
