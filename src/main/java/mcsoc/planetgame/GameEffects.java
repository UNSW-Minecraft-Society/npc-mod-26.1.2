package mcsoc.planetgame;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class GameEffects {    
    private GameEffects() { /* delete */ }

    private static final double DASH_STRENGTH = 0.8;
    public static final double PICKUP_DISTANCE = 1.5;
    private static final double THROW_STRENGTH = 3;
    public static final int DASH_COOLDOWN_TICKS = 20 * 2;
    public static final int THROW_COOLDOWN_TICKS = 20 * 3;
    public static final int DROP_COOLDOWN_TICKS = 20 * 1;
    public static final double XRAY_RANGE = 5;
    public static final int DIG_SIZE = 3/2;

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
        player.setSprinting(false);
        player.teleport(player.getServerWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw() + 180, player.getPitch());
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
                if (GameStateManager.getPlayerGravityDirection(player).equals(Direction.UP)) toggleIsPlayerFlipped(player);
                setPlayerGravityStrength(player, GravityStrength.getDefault());
            }
        }
        GameStateManager.setPlayerInGravityField(player, in_field);
    }

    public static void togglePlayerSecondAbilityState(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) {
            GameStateManager.togglePlayerSecondAbilityState(uuid, server);
            return;
        }
        togglePlayerSecondAbilityState(player);
    }

    public static void togglePlayerSecondAbilityState(ServerPlayerEntity player) {
        // TODO: do some visual effect here
        PlayerSecondAbilities ability = GameStateManager.getPlayerSecondAbility(player);

        if (GameStateManager.getPlayerSecondAbilityState(player)) {
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
    
    private static void drillEffect(ServerPlayerEntity player) {
        HitResult hit_result = player.raycast(3, 0, false);
        if (hit_result.getType() == Type.BLOCK) {
            Vec3d hit_pos = hit_result.getPos();
            breakBlocksBig(player, new BlockPos((int)hit_pos.x, (int)hit_pos.y - 1, (int)hit_pos.z));
        }
    }


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

    public static void triggerPlayerDashAdditive(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerDashAdditive(player);
    }

    public static void triggerPlayerThrow(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) return;
        triggerPlayerThrow(player);
    }

    public static void pickUpEntity(ServerPlayerEntity player, Entity entity) {
        entity.startRiding(player, true);
        GameStateManager.setIfPlayerIsCarrying(player, true);
        storePlayerInventory(player);
    }

    public static Entity dropHeldEntity(ServerPlayerEntity player) {
        Entity first_passenger = player.getFirstPassenger();
        if (Objects.isNull(first_passenger)) return null;

        first_passenger.dismountVehicle();
        Vec3d dismount_offset = player.getRotationVector().multiply(1, 0, 1).normalize().multiply(0.5);
        first_passenger.requestTeleportOffset(dismount_offset.getX(), dismount_offset.getY(), dismount_offset.getZ());
        return first_passenger;
    }

    public static void dropPassengerIntentionally(ServerPlayerEntity player) {
        Entity first_passenger = dropHeldEntity(player);
        if (Objects.isNull(first_passenger)) return;
        
        GameStateManager.setPlayerThirdAbilityCooldownTicks(player, DROP_COOLDOWN_TICKS);
    }

    public static void attemptPickUpNearbyPlayer(ServerPlayerEntity player) {
        PlayerEntity other_player = player.getWorld().getPlayers().stream().filter(p -> p != player && player.distanceTo(p) < PICKUP_DISTANCE).sorted((p1, p2) -> player.distanceTo(p1) > player.distanceTo(p2) ? 1 : -1).findFirst().orElse(null);
        if (Objects.isNull(other_player)) {
            return;
        }
        pickUpEntity(player, other_player);
        other_player.setPose(EntityPose.SLEEPING);
    }

    public static void throwHeldObject(ServerPlayerEntity player) {
        Entity first_passenger = player.getFirstPassenger();
        first_passenger.dismountVehicle();
        first_passenger.addVelocity(player.getRotationVector().multiply(THROW_STRENGTH));
        first_passenger.velocityModified = true;

        GameStateManager.setIfPlayerIsCarrying(player, false);
        attemptReturnPlayerInventory(player);
    }

    public static void storePlayerInventory(ServerPlayerEntity player) {
        PlayerInventory cloned_inventory = new PlayerInventory(player);
        cloned_inventory.clone(player.getInventory());
        GameStateManager.addInventoryToHeap(player, cloned_inventory);
        player.getInventory().clear();
    }

    public static void attemptReturnPlayerInventory(ServerPlayerEntity player) {
        Optional<Inventory> player_inventory_optional = GameStateManager.retrieveOptionalInventoryFromHeap(player);
        if (player_inventory_optional.isEmpty()) return;
        Inventory player_inventory = player_inventory_optional.get();
        for (int i = 0; i < player_inventory.size(); ++i) {
            ItemStack itemStack = player_inventory.getStack(i);
            player.getInventory().setStack(i, itemStack);
        }
    }

    public static void returnPlayerInventory(ServerPlayerEntity player) {
        Inventory player_inventory = GameStateManager.retrieveOptionalInventoryFromHeap(player).orElseThrow();
        for (int i = 0; i < player_inventory.size(); ++i) {
            ItemStack itemStack = player_inventory.getStack(i);
            player.getInventory().setStack(i, itemStack);
        }
    }

    public static void triggerPlayerThrow(ServerPlayerEntity player) {
        // TODO
        if (!GameStateManager.getIfPlayerIsCarrying(player)) {
            attemptPickUpNearbyPlayer(player);
        } else {
            throwHeldObject(player);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(player, THROW_COOLDOWN_TICKS);
        }
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

    public static Text getSecondAbilityActionbarResponse(ServerPlayerEntity player) {
        PlayerSecondAbilities second_ability = GameStateManager.getPlayerSecondAbility(player);
        if (second_ability == PlayerSecondAbilities.XRAY) {
            return Text.of(String.format("xray %s", GameStateManager.getPlayerSecondAbilityState(player) ? "on" : "off"));
        } else if (second_ability == PlayerSecondAbilities.DRILLING) {
            return Text.of(String.format("drill %s", GameStateManager.getPlayerSecondAbilityState(player) ? "on" : "off"));
        }
        return Text.literal("No ability to trigger.");
    }

    public static void sendSecondAbilityActionbarText(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(GameEffects.getSecondAbilityActionbarResponse(player)));
    }

    public static Text getThirdAbilityActionbarResponse(ServerPlayerEntity player) {

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

    public static boolean shouldSendThirdAbilityPerTickActionbarText(ServerPlayerEntity player) {
        int cooldown_ticks_remaining = GameStateManager.getPlayerThirdAbilityCooldownTicks(player);
        return !GameStateManager.getPlayerThirdAbility(player).equals(PlayerThirdAbilities.NONE) && cooldown_ticks_remaining > 0;
    }

    public static void sendThirdAbilityPerTickActionbarText(ServerPlayerEntity player) {
        if (shouldSendThirdAbilityPerTickActionbarText(player)) {
            sendThirdAbilityActionbarText(player);
        }
    }

    public static void sendThirdAbilityActionbarText(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new OverlayMessageS2CPacket(GameEffects.getThirdAbilityActionbarResponse(player)));
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
        sendFirstAbilityActionbarText(player);
    }
    
    public static void triggerSecondAbility(UUID uuid, MinecraftServer server) {
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

    public static void triggerThirdAbility(UUID uuid, MinecraftServer server) {
        if (GameStateManager.getPlayerThirdAbilityCooldownTicks(uuid, server) > 0) return;
        PlayerThirdAbilities third_ability = GameStateManager.getPlayerThirdAbility(uuid, server);
        if (third_ability == PlayerThirdAbilities.DASH_ADDITIVE) {
            //TODO
            GameEffects.triggerPlayerDashAdditive(uuid, server);
            GameStateManager.setPlayerThirdAbilityCooldownTicks(uuid, server, DASH_COOLDOWN_TICKS);
        } else if (third_ability == PlayerThirdAbilities.THROW) {
            //TODO
            GameEffects.triggerPlayerThrow(uuid, server);
        }
    }

    public static void triggerThirdAbility(ServerPlayerEntity player) {
        triggerThirdAbility(player.getUuid(), player.getServer());
        sendThirdAbilityActionbarText(player);
    }


    public static void tick(ServerPlayerEntity player) {
        GameStateManager.tickPlayerState(player);
        GameEffects.sendThirdAbilityPerTickActionbarText(player);
        if (GameStateManager.getPlayerSecondAbility(player) == PlayerSecondAbilities.DRILLING && 
                GameStateManager.getPlayerSecondAbilityState(player)) {
            drillEffect(player);
        }
    }

    public static void tick(UUID uuid, MinecraftServer server) {
        ServerPlayerEntity player = getPlayerFromUuid(uuid, server);
        if (Objects.isNull(player)) {
            GameStateManager.tickPlayerState(uuid, server);
        } else {
            tick(player);
        }
    }
}
