package mcsoc.planetgame.StateManagement;

import mcsoc.planetgame.PlanetGame;

import java.util.Objects;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class GameEffects {
    private GameEffects() { /* delete */ }


    public abstract static class CommandActions {
        private CommandActions() { /* delete */ }

        private static final DynamicCommandExceptionType COULD_NOT_FIND_PLAYER = new DynamicCommandExceptionType(p -> Text.of(String.format("Could not find player %s!", p)));

        public static int flipCallingPlayerCommand(CommandContext<ServerCommandSource> cxt) {

            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameEffects.toggleIsPlayerFlipped(player);
            return 1;
        }

        public static int flipTargetPlayerCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {

            MinecraftServer server = cxt.getSource().getServer();

            String target_player_name = StringArgumentType.getString(cxt, "player name");
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(target_player_name);
            if (Objects.isNull(player)) {
                throw COULD_NOT_FIND_PLAYER.create(target_player_name);
            }

            GameEffects.toggleIsPlayerFlipped(player);
            return 1;
        }
    }


    public static void toggleIsPlayerFlipped(ServerPlayerEntity player) {

        PlanetGame.LOGGER.info("GE: toggled flip");

        GameStateManager.flipPlayerGravity(player);

        ServerPlayNetworking.send(player, GameStateManager.getPlayerStatePacket(player));

        PlanetGame.LOGGER.info("GE: sent packet");
    }
}
