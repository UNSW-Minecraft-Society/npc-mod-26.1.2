package mcsoc.planetgame.registration.entities;

import java.util.Vector;

import mcsoc.planetgame.registration.blocks.crackedblocks.CrackedBricksBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class ThrowableRockEntity extends LivingEntity {
    private ServerPlayerEntity owner;
    private boolean should_kill = false;
    
    public ThrowableRockEntity(EntityType<? extends ThrowableRockEntity> entityType, World world) {
        super(entityType, world);
        this.owner = null;
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
        return !(
            target.equals(getOwner()) || 
            (target instanceof ProjectileEntity)
        );
    }

    protected VoxelShape getBoundingShape() {
        return VoxelShapes.cuboid(this.getBoundingBox().expand(0.1));
    }

    protected boolean isIntersectingWithBlock(BlockState state, World world, BlockPos pos) {
        VoxelShape collision_shape = state.getCollisionShape(world, pos).offset(pos.getX(), pos.getY(), pos.getZ());
        if (collision_shape.isEmpty()) return false;

        return VoxelShapes.matchesAnywhere(this.getBoundingShape(), collision_shape, BooleanBiFunction.AND);
    }

    protected void onHitBlockEffect(BlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        if (block instanceof CrackedBricksBlock brick_block) {
            brick_block.triggerThrowableRockCollision(state, world, pos, this);
        }
    }

    @Override
    public void tick() {
        if (should_kill) {
            this.kill();
        }
        super.tick();

        if (this.hasVehicle()) return;
        
        if (this.isOnGround()) {
            should_kill = true;
        }

        if (this.horizontalCollision) {
            // below loop code taken DIRECTLY from Entity.checkBlockCollision() LMAO
            Box box = this.getBoundingBox().expand(0.1);
            BlockPos min_corner = BlockPos.ofFloored(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
            BlockPos max_corner = BlockPos.ofFloored(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);

            World world = getWorld();
            for (int i = min_corner.getX(); i <= max_corner.getX(); ++i) {
                for (int j = min_corner.getY(); j <= max_corner.getY(); ++j) {
                    for (int k = min_corner.getZ(); k <= max_corner.getZ(); ++k) {
                        
                        BlockPos cursor_pos = new BlockPos(i, j, k);
                        BlockState state = world.getBlockState(cursor_pos);
                        if (this.isIntersectingWithBlock(state, world, cursor_pos)) {
                            this.onHitBlockEffect(state, world, cursor_pos);
                        }
                    }
                }
            }

            should_kill = true;
        }
        
        this.getWorld().getOtherEntities(this, this.getBoundingBox()).forEach(entity -> {
            if (canHitTarget(entity)) entity.kill();
        });
    }

    public void setOwner(ServerPlayerEntity owner) {
        this.owner = owner;
    }

    public ServerPlayerEntity getOwner() {
        return owner;
    }
}
