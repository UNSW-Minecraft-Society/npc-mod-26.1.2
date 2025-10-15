package mcsoc.planetgame.gameeffects;

import java.util.Optional;
import java.util.UUID;

import mcsoc.planetgame.statemanagement.GameStateManager;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class SecondAbilityGameEffects {
    private SecondAbilityGameEffects() { /* delete */ }

    public static final double XRAY_RANGE = 5;
    public static final int DIG_SIZE = 3/2;

    public static void togglePlayerSecondAbilityState(UUID uuid, MinecraftServer server) {
        Optional<ServerPlayerEntity> player = CommonGameEffects.getPlayerFromUuid(uuid, server);
        if (player.isEmpty()) {
            GameStateManager.togglePlayerSecondAbilityState(uuid, server);
        } else {
            togglePlayerSecondAbilityState(player.get());
        }
    }

    public static void togglePlayerSecondAbilityState(ServerPlayerEntity player) {
        // TODO: do some visual effect here
        PlayerSecondAbilities ability = GameStateManager.getPlayerSecondAbility(player);

        if (GameStateManager.getPlayerSecondAbilityState(player) == true) {
            GameStateManager.setPlayerSecondAbilityState(player, false);
            if (ability == PlayerSecondAbilities.DRILLING) {
                player.removeStatusEffect(StatusEffects.SLOWNESS);
            } else if (ability == PlayerSecondAbilities.XRAY) {
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }
        } else {
            if (ability == PlayerSecondAbilities.DRILLING) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, -1, 3, false, false));
            } else if (ability == PlayerSecondAbilities.XRAY) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 1, false, false));
            }
            GameStateManager.setPlayerSecondAbilityState(player, true);
        }
    }

    private static boolean blockIsBreakable(Block block) {
        return !block.equals(Blocks.DIORITE);
    }

    private static boolean shouldDropBlock(Block block) {
        return block instanceof ExperienceDroppingBlock;
    }

    private static BlockPos shiftedBlockPosFromDirection(int i, int j, Direction direction) {
        return switch (direction) {
            case EAST  -> new BlockPos(0, j, i);
            case WEST  -> new BlockPos(-1, j, i);
            case UP    -> new BlockPos(i, 0, j);
            case DOWN  -> new BlockPos(i, 0, j);
            case NORTH -> new BlockPos(i, j, -1);
            case SOUTH -> new BlockPos(i, j, 0);
        };
    }

    private static boolean attemptBlockBreak(World world, ServerPlayerEntity player, BlockPos pos, boolean do_drop) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (blockIsBreakable(block)) {
            BlockState blockState2 = block.onBreak(world, pos, blockState, player);
            if (world.breakBlock(pos, do_drop)) {
                block.onBroken(world, pos, blockState2);
            }
            player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, world.getBlockState(pos)));
            return true;
        }
        return false;
    }

    private static void breakBlocksBig(ServerPlayerEntity player, BlockPos pos) {
        Direction mining_direction = player.getFacing();
        World world = player.getWorld();
        for (int col = -DIG_SIZE; col <= DIG_SIZE; col++) {
            for (int row = -DIG_SIZE; row <= DIG_SIZE; row++) {
                BlockPos shifted_pos = pos.add(shiftedBlockPosFromDirection(col, row, mining_direction));
                attemptBlockBreak(world, player, shifted_pos, shouldDropBlock(world.getBlockState(pos).getBlock()));
            }
        }
    }
    
    public static void drillBlocksAction(ServerPlayerEntity player) {
        HitResult hit_result = player.raycast(3, 0, false);
        if (hit_result.getType() == Type.BLOCK) {
            Vec3d hit_pos = hit_result.getPos();
            breakBlocksBig(player, new BlockPos((int)hit_pos.x, (int)hit_pos.y - 1, (int)hit_pos.z));
        }
    }

    private static Text getSecondAbilityActionbarResponse(ServerPlayerEntity player) {
        PlayerSecondAbilities second_ability = GameStateManager.getPlayerSecondAbility(player);
        if (second_ability == PlayerSecondAbilities.XRAY) {
            return Text.of(String.format("xray %s", GameStateManager.getPlayerSecondAbilityState(player) ? "on" : "off"));
        } else if (second_ability == PlayerSecondAbilities.DRILLING) {
            return Text.of(String.format("drill %s", GameStateManager.getPlayerSecondAbilityState(player) ? "on" : "off"));
        }
        return Text.literal("No ability to trigger.");
    }

    public static void sendSecondAbilityActionbarText(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(getSecondAbilityActionbarResponse(player)));
    }


    private static void triggerSecondAbility(UUID uuid, MinecraftServer server) {
        PlayerSecondAbilities second_ability = GameStateManager.getPlayerSecondAbility(uuid, server);
        if (second_ability == PlayerSecondAbilities.DRILLING) {
            //TODO
            togglePlayerSecondAbilityState(uuid, server);
        } else if (second_ability == PlayerSecondAbilities.XRAY) {
            // TODO
            togglePlayerSecondAbilityState(uuid, server);
        }
    }

    public static void triggerSecondAbility(ServerPlayerEntity player) {
        triggerSecondAbility(player.getUuid(), player.getServer());
        sendSecondAbilityActionbarText(player);
    }


    protected static void secondAbilityTickAction(ServerPlayerEntity player) {
        if (GameStateManager.getPlayerSecondAbility(player) == PlayerSecondAbilities.DRILLING && 
                GameStateManager.getPlayerSecondAbilityState(player)) {
            drillBlocksAction(player);
        }
    }
}
