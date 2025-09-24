package mcsoc.planetgame.registration.entities;

import java.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
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


    @Override
    public boolean isInsideWall() {
        return false;
    }

    protected boolean canHitTarget(Entity target) {
        return !(//(target instanceof PlayerEntity) || 
        (target instanceof ProjectileEntity));
    }


    protected boolean isIntersectingWithBlock(BlockPos pos) {
        VoxelShape collision_shape = this.getWorld().getBlockState(pos).getCollisionShape(getWorld(), pos);
        return VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(this.getBoundingBox()), collision_shape, BooleanBiFunction.AND);
    }

    @Override
    public void tick() {
        super.tick();
        
        if (this.hasVehicle()) return;

        if (this.verticalCollision && this.isOnGround()) {
            this.kill();
        }
        
        // Vec3d curr_pos = this.getPos();
        this.getWorld().getOtherEntities(this, this.getBoundingBox()).forEach(entity -> {
            if (canHitTarget(entity)) entity.kill();
        });
    }

}
