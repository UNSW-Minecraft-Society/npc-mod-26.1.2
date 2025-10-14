package mcsoc.planetgame.gameeffects;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import mcsoc.planetgame.statemanagement.GameStateManager;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import net.minecraft.block.entity.VaultBlockEntity.Server;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ThirdAbilityGameEffects {


    private static final double DASH_STRENGTH = 0.8;
    private static final double PICKUP_DISTANCE = 1.5;
    private static final double THROW_STRENGTH = 3;
    private static final int DASH_COOLDOWN_TICKS = 20 * 2;
    private static final int THROW_COOLDOWN_TICKS = 20 * 3;
    private static final int DROP_COOLDOWN_TICKS = 20 * 1;

    public static void triggerPlayerDashAdditive(ServerPlayerEntity player) {
        Vec3d unitRotVecHorizontal = player.getRotationVector().multiply(1, 0, 1).normalize().multiply(DASH_STRENGTH);
        player.addVelocity(unitRotVecHorizontal);
        if (GameStateManager.getPlayerGravityDirection(player).equals(Direction.UP)) {
            player.setVelocity(player.getVelocity().multiply(-1, 0, -1));
        } else {
            player.setVelocity(player.getVelocity().multiply(1, 0, 1));
        }
        player.velocityModified = true;
    }

    private static void triggerPlayerDashAdditive(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = CommonGameEffects.getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAdditive(player);
    }

    public static void attemptPickUpNearbyPlayer(ServerPlayerEntity player, double pickup_range) {
        Optional<? extends PlayerEntity> other_player_optional = player.getWorld().getPlayers().stream()
            .filter(p -> !p.equals(player) && player.distanceTo(p) <= pickup_range)
            .sorted((p1, p2) -> player.distanceTo(p1) > player.distanceTo(p2) ? 1 : -1)
            .findFirst();
        if (other_player_optional.isEmpty()) {
            return;
        }
        PlayerEntity other_player = other_player_optional.get();

        CommonGameEffects.pickUpEntity(player, other_player);
        other_player.setPose(EntityPose.SLEEPING);
    }

    private static void triggerPlayerThrow(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = CommonGameEffects.getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerThrow(player);
    }

    public static void triggerPlayerThrow(ServerPlayerEntity player) {
        // TODO
        if (!GameStateManager.getIfPlayerIsCarrying(player)) {
            attemptPickUpNearbyPlayer(player, PICKUP_DISTANCE);
        } else {
            CommonGameEffects.throwHeldObject(player, THROW_STRENGTH);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(player, THROW_COOLDOWN_TICKS);
        }
    }

    public static void triggerPlayerDrop(ServerPlayerEntity player) {
        if (CommonGameEffects.dropPassengerIntentionally(player)) {
            GameStateManager.setPlayerThirdAbilityCooldownTicks(player, DROP_COOLDOWN_TICKS);
        }
    }


    private static Text getThirdAbilityActionbarResponse(ServerPlayerEntity player) {

        PlayerThirdAbilities third_ability = GameStateManager.getPlayerThirdAbility(player);
        if (third_ability == PlayerThirdAbilities.NONE) return Text.literal("No ability to trigger.");

        int cooldown_ticks_remaining = GameStateManager.getPlayerThirdAbilityCooldownTicks(player);

        if (third_ability == PlayerThirdAbilities.DASH_ADDITIVE) {
            if (cooldown_ticks_remaining > 1) {
                return Text.of(String.format("dash cooldown: %.1f s", (double)(cooldown_ticks_remaining) / 20));
            } else {
                return Text.literal("dash ready");
            }
        } else if (third_ability == PlayerThirdAbilities.THROW) {
            if (cooldown_ticks_remaining > 1) {
                return Text.of(String.format("throw cooldown: %.1f s", (double)(cooldown_ticks_remaining) / 20));
            } else {
                return Text.literal("throw ready");
            }
        }
        return Text.literal("third ability action bar response unimplemented?");
    }

    private static boolean shouldSendThirdAbilityPerTickActionbarText(ServerPlayerEntity player) {
        int cooldown_ticks_remaining = GameStateManager.getPlayerThirdAbilityCooldownTicks(player);
        return !GameStateManager.getPlayerThirdAbility(player).equals(PlayerThirdAbilities.NONE) && cooldown_ticks_remaining > 0;
    }

    private static void sendThirdAbilityActionbarText(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(getThirdAbilityActionbarResponse(player)));
    }

    public static void sendThirdAbilityPerTickActionbarText(ServerPlayerEntity player) {
        if (shouldSendThirdAbilityPerTickActionbarText(player)) {
            sendThirdAbilityActionbarText(player);
        }
    }


    private static void triggerThirdAbility(UUID uuid, MinecraftServer server) {
        if (GameStateManager.getPlayerThirdAbilityCooldownTicks(uuid, server) > 0) return;
        PlayerThirdAbilities third_ability = GameStateManager.getPlayerThirdAbility(uuid, server);
        if (third_ability == PlayerThirdAbilities.DASH_ADDITIVE) {
            //TODO
            triggerPlayerDashAdditive(uuid, server);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(uuid, server, DASH_COOLDOWN_TICKS);
        } else if (third_ability == PlayerThirdAbilities.THROW) {
            //TODO
            triggerPlayerThrow(uuid, server);
        }
    }

    public static void triggerThirdAbility(ServerPlayerEntity player) {
        triggerThirdAbility(player.getUuid(), player.getServer());
        sendThirdAbilityActionbarText(player);
    }


    protected static void thirdAbilityTickAction(ServerPlayerEntity player) {
        sendThirdAbilityPerTickActionbarText(player);
    }

}
