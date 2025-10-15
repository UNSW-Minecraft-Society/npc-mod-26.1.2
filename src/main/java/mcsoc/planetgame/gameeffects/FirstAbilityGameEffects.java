package mcsoc.planetgame.gameeffects;

import java.util.Optional;
import java.util.UUID;

import mcsoc.planetgame.statemanagement.GameStateManager;
import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

public abstract class FirstAbilityGameEffects {
    private FirstAbilityGameEffects() { /* delete */ }

    protected static void toggleNextGravityStrength(UUID uuid, MinecraftServer server) {
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


    protected static void flipPlayerGravity(UUID uuid, MinecraftServer server) {
        Optional<ServerPlayerEntity> player = CommonGameEffects.getPlayerFromUuid(uuid, server);
        if (player.isEmpty()) {
            GameStateManager.flipPlayerGravity(uuid, server);
        } else {
            flipPlayerGravity(player.get());
        }
    }

    public static void flipPlayerGravity(ServerPlayerEntity player) {
        player.setSprinting(false);
        player.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw() + 180, player.getPitch());
        GameStateManager.flipPlayerGravity(player);
    }


    public static void setPlayerInGravityField(ServerPlayerEntity player, boolean in_field) {
        // TODO: do some visual effect here
        if (GameStateManager.getPlayerInGravityField(player) != in_field) {
            if (in_field) {
                player.sendMessage(Text.literal("entered field"));
            } else {
                player.sendMessage(Text.literal("exited field"));
                if (GameStateManager.getPlayerGravityDirection(player).equals(Direction.UP)) flipPlayerGravity(player);
                GameStateManager.setPlayerGravityStrength(player, GravityStrength.getDefault());
            }
        }
        GameStateManager.setPlayerInGravityField(player, in_field);
    }

    
    private static void triggerFirstAbility(UUID uuid, MinecraftServer server) {
        if (!GameStateManager.getPlayerInGravityField(uuid, server)) return;

        PlayerFirstAbilities first_ability = GameStateManager.getPlayerFirstAbility(uuid, server);
        if (first_ability == PlayerFirstAbilities.FLIP) {
            FirstAbilityGameEffects.flipPlayerGravity(uuid, server);
        } else if (first_ability == PlayerFirstAbilities.CONTROL) {
            FirstAbilityGameEffects.toggleNextGravityStrength(uuid, server);
        }
    }


    private static Text getFirstAbilityActionbarResponse(ServerPlayerEntity player) {
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
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(getFirstAbilityActionbarResponse(player)));
    }

    public static void triggerFirstAbility(ServerPlayerEntity player) {
        triggerFirstAbility(player.getUuid(), player.getServer());
        sendFirstAbilityActionbarText(player);
    }

    protected static void firstAbilityTickAction(ServerPlayerEntity player) {

    }
}
