package mcsoc.planetgame.commands;

import java.util.Optional;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import mcsoc.planetgame.gameeffects.FirstAbilityGameEffects;
import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import mcsoc.planetgame.statemanagement.gamestate.GameStateManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public abstract class CommandActions {
    private CommandActions() { /* delete */ }

    private static final DynamicCommandExceptionType COULD_NOT_FIND_PLAYER = new DynamicCommandExceptionType(p -> Text.of(String.format("Could not find player %s!", p)));

    private static ServerPlayerEntity getPlayerFromName(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        String target_player_name = StringArgumentType.getString(cxt, CommandRegistrationHandler.PLAYER_NAME_ARGUMENT);
        Optional<ServerPlayerEntity> player_optional = Optional.ofNullable(cxt.getSource().getServer().getPlayerManager().getPlayer(target_player_name));
        if (player_optional.isEmpty()) {
            throw COULD_NOT_FIND_PLAYER.create(target_player_name);
        }
        return player_optional.get();
    }


    public static int flipCallingPlayerCommand(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        FirstAbilityGameEffects.flipPlayerGravity(player);
        return 1;
    }

    public static int flipTargetPlayerCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        FirstAbilityGameEffects.flipPlayerGravity(player);
        return 1;
    }


    public static int setCallingPlayerGravityStrengthCommand(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
        GameStateManager.setPlayerGravityStrength(player, GravityStrength.fromDouble(new_grav_strength));
        return 1;
    }

    public static int setTargetPlayerGravityStrengthCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
        GameStateManager.setPlayerGravityStrength(player, GravityStrength.fromDouble(new_grav_strength));
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


    public static int setCallingPlayerSecondAbilityNone(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.NONE);
        return 1;
    }

    public static int setTargetPlayerSecondAbilityNone(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.NONE);
        return 1;
    }

    public static int setCallingPlayerSecondAbilityXray(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.XRAY);
        return 1;
    }

    public static int setTargetPlayerSecondAbilityXray(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.XRAY);
        return 1;
    }

    public static int setCallingPlayerSecondAbilityDrill(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.DRILLING);
        return 1;
    }

    public static int setTargetPlayerSecondAbilityDrill(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerSecondAbility(player, PlayerSecondAbilities.DRILLING);
        return 1;
    }


    public static int setCallingPlayerThirdAbilityNone(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.NONE);
        return 1;
    }

    public static int setTargetPlayerThirdAbilityNone(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.NONE);
        return 1;
    }

    public static int setCallingPlayerThirdAbilityDash(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.DASH_ADDITIVE);
        return 1;
    }

    public static int setTargetPlayerThirdAbilityDash(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.DASH_ADDITIVE);
        return 1;
    }

    public static int setCallingPlayerThirdAbilityThrow(CommandContext<ServerCommandSource> cxt) {
        ServerPlayerEntity player = cxt.getSource().getPlayer();
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.THROW);
        return 1;
    }

    public static int setTargetPlayerThirdAbilityThrow(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
        ServerPlayerEntity player = getPlayerFromName(cxt);
        GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.THROW);
        return 1;
    }
}