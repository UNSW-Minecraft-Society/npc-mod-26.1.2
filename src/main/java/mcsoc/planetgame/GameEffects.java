package mcsoc.planetgame;

import java.util.Objects;
import java.util.UUID;

import mcsoc.planetgame.registration.CommandRegistrationHandler;
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public abstract class GameEffects {    
    private GameEffects() { /* delete */ }

    private static final double DASH_STRENGTH = 0.8;
    public static final double PICKUP_DISTANCE = 1.5;
    private static final double THROW_STRENGTH = 3;
    public static final int DASH_COOLDOWN_TICKS = 20 * 2;
    public static final int THROW_COOLDOWN_TICKS = 20 * 3;

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


        public static int setCallingPlayerGravityStrengthCommand(CommandContext<ServerCommandSource> cxt) {
            ServerPlayerEntity player = cxt.getSource().getPlayer();
            Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
            setPlayerGravityStrength(player, GravityStrength.fromDouble(new_grav_strength));
            return 1;
        }

        public static int setTargetPlayerGravityStrengthCommand(CommandContext<ServerCommandSource> cxt) throws CommandSyntaxException {
            ServerPlayerEntity player = getPlayerFromName(cxt);
            Double new_grav_strength = DoubleArgumentType.getDouble(cxt, CommandRegistrationHandler.GRAVITY_STRENGTH_ARGUMENT);
            setPlayerGravityStrength(player, GravityStrength.fromDouble(new_grav_strength));
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


    public static void setPlayerGravityStrength(ServerPlayerEntity player, GravityStrength grav_strength) {
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
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) {
            GameStateManager.flipPlayerGravity(uuid, server);
            return;
        }
        toggleIsPlayerFlipped(player);
    }

    public static void toggleIsPlayerFlipped(ServerPlayerEntity player) {
        // player.setYaw(180 + player.getYaw());
        // player.networkHandler.requestTeleport(
        //     player.getX(), player.getY(), player.getZ(), 
        //     player.getYaw() + 180, player.getPitch()
        // );
        player.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw() + 180, player.getPitch());
        // player.getServer().getPlayerManager().getPlayerList().forEach(p2 -> {
        //     p2.networkHandler.sendPacket(new EntityPositionS2CPacket(player));
        // });
        GameStateManager.flipPlayerGravity(player);
    }

    public static void setPlayerInGravityField(UUID uuid, MinecraftServer server, boolean in_field) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) {
            GameStateManager.flipPlayerGravity(uuid, server);
            return;
        }
        setPlayerInGravityField(player, in_field);
    }

    public static void setPlayerInGravityField(ServerPlayerEntity player, boolean in_field) {
        // TODO: do some visual effect here
        if (GameStateManager.getPlayerInGravityField(player) != in_field) {
            if (in_field) {
                player.sendMessage(Text.literal("entered field"));
            } else {
                player.sendMessage(Text.literal("exited field"));
                if (GameStateManager.getPlayerGravityDirection(player).equals(Direction.UP))
                toggleIsPlayerFlipped(player);
                setPlayerGravityStrength(player, GravityStrength.getDefault());
            }
        }
        GameStateManager.setPlayerInGravityField(player, in_field);
    }


    public static void triggerPlayerDashAdditive(ServerPlayerEntity player) {
        Vec3d unitRotVecHorizontal = player.getRotationVector().multiply(1, 0, 1).normalize().multiply(DASH_STRENGTH);
        player.addVelocity(unitRotVecHorizontal);
        player.setVelocity(player.getVelocity().multiply(1, 0, 1));
        player.velocityModified = true;
        GameStateManager.setPlayerGravityModified(player);
    }

    public static void triggerPlayerDashAdditive(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAdditive(player);
    }

    public static void triggerPlayerDashAbsolute(ServerPlayerEntity player) {
        Vec3d unitRotVecHorizontal = player.getRotationVector().multiply(1, 0, 1).normalize().multiply(DASH_STRENGTH);
        player.setVelocity(unitRotVecHorizontal);
        player.velocityModified = true;
    }

    public static void triggerPlayerDashAbsolute(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAbsolute(player);
    }

    public static boolean getIsPlayerCarryingSomething(ServerPlayerEntity player) {
        return player.hasPassengers();
    }

    public static void triggerPlayerThrow(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerThrow(player);
    }

    public static void pickUpEntity(ServerPlayerEntity player, LivingEntity entity) {
        entity.startRiding(player, true);
    }

    public static void attemptPickUpNearbyPlayer(ServerPlayerEntity player) {
        PlayerEntity other_player = player.getWorld().getPlayers().stream().filter(p -> p != player && player.distanceTo(p) < PICKUP_DISTANCE).sorted((p1, p2) -> player.distanceTo(p1) > player.distanceTo(p2) ? 1 : -1).findFirst().orElse(null);
        if (Objects.isNull(other_player)) {
            return;
        }
        pickUpEntity(player, other_player);
        player.setPose(EntityPose.SLEEPING);
    }

    public static void throwHeldObject(ServerPlayerEntity player) {
        Entity first_passenger = player.getFirstPassenger();
        first_passenger.dismountVehicle();
        first_passenger.addVelocity(player.getRotationVector().multiply(THROW_STRENGTH));
        first_passenger.velocityModified = true;
    }

    public static void triggerPlayerThrow(ServerPlayerEntity player) {
        // TODO
        if (!getIsPlayerCarryingSomething(player)) {
            attemptPickUpNearbyPlayer(player);
        } else {
            throwHeldObject(player);
        }
    }


    public static void triggerFirstAbility(UUID uuid, MinecraftServer server) {
        if (!GameStateManager.getPlayerInGravityField(uuid, server)) return;

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
            GameEffects.triggerPlayerThrow(uuid, server);
        }
    }

    public static void triggerThirdAbility(ServerPlayerEntity player) {
        triggerThirdAbility(player.getUuid(), player.getServer());
    }
    

    public static Text getFirstAbilityActionbarResponse(ServerPlayerEntity player) {
        if (!GameStateManager.getPlayerInGravityField(player)) {
            return Text.literal("must be in gravity field to trigger");
        }
        PlayerFirstAbilities first_ability = GameStateManager.getPlayerFirstAbility(player);
        if (first_ability == PlayerFirstAbilities.FLIP) {
            return Text.of(String.format("gravity direction: %s", GameStateManager.getPlayerGravityDirection(player)));
        } else if (first_ability == PlayerFirstAbilities.CONTROL) {
            return Text.of(String.format("gravity strength: %.1f", GameStateManager.getPlayerGravityStrength(player).getDouble()));
        }
        return Text.literal("No ability to trigger.");
    }

    public static void sendFirstAbilityActionbarText(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(GameEffects.getFirstAbilityActionbarResponse(player)));
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
