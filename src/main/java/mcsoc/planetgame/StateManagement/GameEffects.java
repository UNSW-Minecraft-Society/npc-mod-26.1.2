package mcsoc.planetgame.statemanagement;

import java.util.Objects;
import java.util.UUID;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import mcsoc.planetgame.eventhandlers.CommandRegistrationHandler;
import mcsoc.planetgame.statemanagement.PlayerState.GravityStrength;
import mcsoc.planetgame.statemanagement.PlayerState.PlayerAbilities1;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class GameEffects {
    private GameEffects() { /* delete */ }

    public abstract static class CommandActions {
        private CommandActions() { /* delete */ }

        private static final DynamicCommandExceptionType COULD_NOT_FIND_PLAYER = new DynamicCommandExceptionType(p -> Text.of(String.format("Could not find player %s!", p)));

        private static ServerPlayerEntity getPlayerFromName(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            String target_player_name = StringArgumentType.getString(cxt, CommandRegistrationHandler.PLAYER_NAME_ARGUMENT);
            ServerPlayerEntity player = cxt.getSource().getServer().getPlayerManager().getPlayer(target_player_name);
            if (Objects.isNull(player)) {
                throw COULD_NOT_FIND_PLAYER.create(target_player_name);
            }
            return player;
        }

        public static int flipCallingPlayerCommand(CommandContext<ServerCommandSource> cxt) {

            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameEffects.toggleIsPlayerFlipped(player);
            return 1;
        }

        public static int flipTargetPlayerCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            GameEffects.toggleIsPlayerFlipped(player);
            return 1;
        }


        public static int setCallingPlayerGravStrengthCommand(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
            setPlayerGravStrength(player, new_grav_strength);
            return 1;
        }

        public static int setTargetPlayerGravStrengthCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
            setPlayerGravStrength(player, new_grav_strength);
            return 1;
        }
    }

    public static void setPlayerGravStrength(ServerPlayerEntity player, Double grav_strength) {
        GameStateManager.setPlayerGravityStrength(player, grav_strength);
    }


    public static void toggleIsPlayerFlipped(ServerPlayerEntity player) {
        GameStateManager.flipPlayerGravity(player);
    }
}
