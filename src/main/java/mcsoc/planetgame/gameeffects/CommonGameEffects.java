package mcsoc.planetgame.gameeffects;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import mcsoc.planetgame.statemanagement.GameStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public abstract class CommonGameEffects {    
    private CommonGameEffects() { /* delete */ }


    
    public static ServerPlayerEntity getPlayerFromUuid(UUID uuid, MinecraftServer server) {
        return server.getPlayerManager().getPlayer(uuid);
    }

    
    public static void storePlayerInventory(ServerPlayerEntity player) {
        PlayerInventory cloned_inventory = new PlayerInventory(player);
        cloned_inventory.clone(player.getInventory());
        GameStateManager.addInventoryToHeap(player, cloned_inventory);
        player.getInventory().clear();
    }

    private static void returnPlayerInventory(ServerPlayerEntity player, Inventory player_inventory) {
        for (int i = 0; i < player_inventory.size(); ++i) {
            ItemStack itemStack = player_inventory.getStack(i);
            player.getInventory().setStack(i, itemStack);
        }
    }

    public static void attemptReturnPlayerInventory(ServerPlayerEntity player) {
        Optional<Inventory> player_inventory_optional = GameStateManager.retrieveOptionalInventoryFromHeap(player);
        if (player_inventory_optional.isEmpty()) return;
        Inventory player_inventory = player_inventory_optional.get();
        returnPlayerInventory(player, player_inventory);
    }


    public static void pickUpEntity(ServerPlayerEntity player, Entity entity) {
        entity.startRiding(player, true);
        GameStateManager.setIfPlayerIsCarrying(player, true);
        CommonGameEffects.storePlayerInventory(player);
    }

    public static Entity dropHeldEntity(ServerPlayerEntity player) {
        Entity first_passenger = player.getFirstPassenger();
        if (Objects.isNull(first_passenger)) return null;

        first_passenger.dismountVehicle();
        Vec3d dismount_offset = player.getRotationVector().multiply(1, 0, 1).normalize().multiply(0.5);
        first_passenger.requestTeleportOffset(dismount_offset.getX(), dismount_offset.getY(), dismount_offset.getZ());
        return first_passenger;
    }

    public static boolean dropPassengerIntentionally(ServerPlayerEntity player) {
        Entity first_passenger = dropHeldEntity(player);
        return !Objects.isNull(first_passenger);
    }

    public static void throwHeldObject(ServerPlayerEntity player, double throw_strength) {
        Entity first_passenger = player.getFirstPassenger();
        first_passenger.dismountVehicle();
        first_passenger.addVelocity(player.getRotationVector().multiply(throw_strength));
        first_passenger.velocityModified = true;

        GameStateManager.setIfPlayerIsCarrying(player, false);
        CommonGameEffects.attemptReturnPlayerInventory(player);
    }


    public static void tick(ServerPlayerEntity player) {
        GameStateManager.tickPlayerState(player);
        
        FirstAbilityGameEffects.firstAbilityTickAction(player);
        SecondAbilityGameEffects.secondAbilityTickAction(player);
        ThirdAbilityGameEffects.thirdAbilityTickAction(player);
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
