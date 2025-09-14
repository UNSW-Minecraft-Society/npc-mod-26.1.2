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
import mcsoc.planetgame.statemanagement.PlayerState.PlayerFirstAbilities;
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
            setPlayerGravStrength(player, GravityStrength.fromDouble(new_grav_strength));
            return 1;
        }

        public static int setTargetPlayerGravStrengthCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
            setPlayerGravStrength(player, GravityStrength.fromDouble(new_grav_strength));
            return 1;
        }

        public static int setCallingPlayerFirstAbilityNone(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.NONE);
            return 1;
        }

        public static int setTargetPlayerFirstAbilityNone(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.NONE);
            return 1;
        }

        public static int setCallingPlayerFirstAbilityFlip(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.FLIP);
            return 1;
        }

        public static int setTargetPlayerFirstAbilityFlip(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.FLIP);
            return 1;
        }

        public static int setCallingPlayerFirstAbilityControl(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.CONTROL);
            return 1;
        }

        public static int setTargetPlayerFirstAbilityControl(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            GameStateManager.setPlayerFirstAbility(player, PlayerFirstAbilities.CONTROL);
            return 1;
        }
    }

    public static void setPlayerGravStrength(ServerPlayerEntity player, GravityStrength grav_strength) {
        GameStateManager.setPlayerGravityStrength(player, grav_strength);
    }

    public static void toggleNextGravityStrength(UUID uuid, MinecraftServer server) {
        GravityStrength new_grav_strength = switch (GameStateManager.getPlayerGravityStrength(uuid, server)) {
            case GRAV_STRENGTH_HIGH -> GravityStrength.GRAV_STRENGTH_LOW;
            case GRAV_STRENGTH_LOW -> GravityStrength.GRAV_STRENGTH_NORMAL;
            case GRAV_STRENGTH_NORMAL -> GravityStrength.GRAV_STRENGTH_HIGH;
            default -> GravityStrength.GRAV_STRENGTH_NONE;
        };

        GameStateManager.setPlayerGravityStrength(uuid, server, new_grav_strength);
    }

    public static void toggleNextGravityStrength(ServerPlayerEntity player) {
        toggleNextGravityStrength(player.getUuid(), player.getServer());
    }


    public static void toggleIsPlayerFlipped(UUID uuid, MinecraftServer server) {
        GameStateManager.flipPlayerGravity(uuid, server);
    }

    public static void toggleIsPlayerFlipped(ServerPlayerEntity player) {
        toggleIsPlayerFlipped(player.getUuid(), player.getServer());
    }


    public static void triggerGravAbility(UUID uuid, MinecraftServer server) {
        if (GameStateManager.getPlayerFirstAbility(uuid, server) == PlayerFirstAbilities.FLIP) {
            GameEffects.toggleIsPlayerFlipped(uuid, server);
        } else if (GameStateManager.getPlayerFirstAbility(uuid, server) == PlayerFirstAbilities.CONTROL) {
            GameEffects.toggleNextGravityStrength(uuid, server);
        }
    }

    public static void triggerGravAbility(ServerPlayerEntity player) {
        triggerGravAbility(player.getUuid(), player.getServer());
    }
}
