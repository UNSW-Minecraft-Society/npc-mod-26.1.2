package mcsoc.planetgame;

import java.util.Objects;
import java.util.UUID;

import mcsoc.planetgame.eventhandlers.CommandRegistrationHandler;
import mcsoc.planetgame.statemanagement.GameStateManager;
import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public abstract class GameEffects {    
    private GameEffects() { /* delete */ }

    private static final double DASH_STRENGTH = 5;
    public static final int DASH_COOLDOWN_TICKS = 20 * 3;
    public static final int THROW_COOLDOWN_TICKS = 20 * 5;

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

        public static int setCallingPlayerThirdAbilityDashAbsolute(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.DASH_SET);
            return 1;
        }

        public static int setTargetPlayerThirdAbilityDashAbsolute(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            GameStateManager.setPlayerThirdAbility(player, PlayerThirdAbilities.DASH_SET);
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


    private static ServerPlayerEntity getPlayerFromUuid(UUID uuid, MinecraftServer server) {
            return server.getPlayerManager().getPlayer(uuid);
        }


    public static void setPlayerGravStrength(ServerPlayerEntity player, GravityStrength grav_strength) {
        GameStateManager.setPlayerGravityStrength(player, grav_strength);
    }

    public static void toggleNextGravityStrength(UUID uuid, MinecraftServer server) {
        GravityStrength new_grav_strength = switch (GameStateManager.getPlayerGravityStrength(uuid, server)) {
            case GRAV_STRENGTH_HIGH -> GravityStrength.GRAV_STRENGTH_NORMAL;
            case GRAV_STRENGTH_LOW -> GravityStrength.GRAV_STRENGTH_HIGH;
            case GRAV_STRENGTH_NORMAL -> GravityStrength.GRAV_STRENGTH_LOW;
            case GRAV_STRENGTH_NONE -> GravityStrength.GRAV_STRENGTH_NORMAL;
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


    public static void triggerPlayerDashAdditive(ServerPlayerEntity player) {
        Vec3d unitRotVecHorizontal = player.getRotationVector().multiply(1, 0, 1).normalize();
        player.addVelocity(unitRotVecHorizontal.multiply(DASH_STRENGTH));
        player.velocityModified = true;
        GameStateManager.setPlayerGravModified(player);
    }

    public static void triggerPlayerDashAdditive(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAdditive(player);
    }

    public static void triggerPlayerDashAbsolute(ServerPlayerEntity player) {
        Vec3d unitRotVecHorizontal = player.getRotationVector().multiply(1, 0, 1).normalize();
        player.setVelocity(unitRotVecHorizontal.multiply(DASH_STRENGTH));
        player.velocityModified = true;
    }

    public static void triggerPlayerDashAbsolute(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAbsolute(player);
    }


    public static void triggerPlayerThrow(UUID uuid, MinecraftServer server) {
        // TODO
    }

    public static void triggerPlayerThrow(ServerPlayerEntity player) {
        triggerPlayerThrow(player.getUuid(), player.getServer());
    }


    public static void triggerFirstAbility(UUID uuid, MinecraftServer server) {
        PlayerFirstAbilities first_ability = GameStateManager.getPlayerFirstAbility(uuid, server);
        if (first_ability == PlayerFirstAbilities.FLIP) {
            GameEffects.toggleIsPlayerFlipped(uuid, server);
        } else if (first_ability == PlayerFirstAbilities.CONTROL) {
            GameEffects.toggleNextGravityStrength(uuid, server);
        }
    }

    public static void triggerFirstAbility(ServerPlayerEntity player) {
        triggerFirstAbility(player.getUuid(), player.getServer());
    }


    public static void triggerThirdAbility(UUID uuid, MinecraftServer server) {
        if (GameStateManager.getPlayerThirdAbilityCooldownTicks(uuid, server) > 0) return;
        PlayerThirdAbilities third_ability = GameStateManager.getPlayerThirdAbility(uuid, server);
        if (third_ability == PlayerThirdAbilities.DASH_ADDITIVE) {
            //TODO
            GameEffects.triggerPlayerDashAdditive(uuid, server);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(uuid, server, DASH_COOLDOWN_TICKS);
        } else if (third_ability == PlayerThirdAbilities.DASH_SET) {
            //TODO
            GameEffects.triggerPlayerDashAbsolute(uuid, server);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(uuid, server, DASH_COOLDOWN_TICKS);
        } else if (third_ability == PlayerThirdAbilities.THROW) {
            //TODO
            //GameEffects.triggerPlayerThrow(uuid, server);
        }
    }

    public static void triggerThirdAbility(ServerPlayerEntity player) {
        triggerThirdAbility(player.getUuid(), player.getServer());
    }
    

    public static Text getFirstAbilityActionbarResponse(ServerPlayerEntity player) {
        PlayerFirstAbilities first_ability = GameStateManager.getPlayerFirstAbility(player);
        if (first_ability == PlayerFirstAbilities.FLIP) {
            return Text.of(String.format("gravity direction: %s", GameStateManager.getPlayerGravityDirection(player)));
        } else if (first_ability == PlayerFirstAbilities.CONTROL) {
            return Text.of(String.format("gravity strength: %.1f", GameStateManager.getPlayerGravityStrength(player).getDouble()));
        }
        return Text.literal("No ability to trigger.");
    }


    public static Text getThirdAbilityActionbarResponse(ServerPlayerEntity player) {
        PlayerThirdAbilities third_ability = GameStateManager.getPlayerThirdAbility(player);
        if (third_ability == PlayerThirdAbilities.DASH_ADDITIVE || third_ability == PlayerThirdAbilities.DASH_SET) {
            // smth about cooldowns
            int cooldown_ticks_remaining = GameStateManager.getPlayerThirdAbilityCooldownTicks(player);
            if (cooldown_ticks_remaining > 1) {
                return Text.of(String.format("dash cooldown: %.1f s", (double)(cooldown_ticks_remaining) / 20));
            } else {
                return Text.of(String.format("dash ready"));
            }
        } else if (third_ability == PlayerThirdAbilities.THROW) {
            // smth about throw idk what
            return Text.literal("Ability Unimplemented");
            // return Text.of(String.format("throw cooldown: %d s", GameStateManager.getPlayerThirdAbilityCooldownTicks(player) / 20));
        }
        return Text.literal("No ability to trigger.");
    }

    public static Boolean shouldSendThirdAbilityActionbarText(ServerPlayerEntity player) {
        int cooldown_ticks_remaining = GameStateManager.getPlayerThirdAbilityCooldownTicks(player);
        return cooldown_ticks_remaining > 0;
    }

    public static void sendThirdAbilityActionbarText(ServerPlayerEntity player) {
        if (shouldSendThirdAbilityActionbarText(player)) {
            player.networkHandler.sendPacket(new OverlayMessageS2CPacket(GameEffects.getThirdAbilityActionbarResponse(player)));
        }
    }


    public static void tickPlayerState(ServerPlayerEntity player) {
        GameStateManager.tickPlayerState(player);
        GameEffects.sendThirdAbilityActionbarText(player);
    }

    public static void tickPlayerState(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) {
            GameStateManager.tickPlayerState(uuid, server);
        } else {
            tickPlayerState(player);
        }
    }
}
