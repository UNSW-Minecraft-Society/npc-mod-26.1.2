package mcsoc.planetgame.registration.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

public class ThrowableRockEntity extends LivingEntity {
    
    public ThrowableRockEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        // no-op
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return new Vector<>();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }

}
