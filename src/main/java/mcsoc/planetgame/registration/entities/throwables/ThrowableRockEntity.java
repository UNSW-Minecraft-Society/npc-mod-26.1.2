package mcsoc.planetgame.registration.entities.throwables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThrowableRockEntity extends ThrowableEntity {

    public ThrowableRockEntity(EntityType<? extends ThrowableRockEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected boolean onHitBlockEffect(BlockState state, World world, BlockPos pos) {
        Block block = state.getBlock();
        if (block instanceof ThrowableInteractible interactible) {
            interactible.triggerThrowableCollision(state, world, pos, this);
            return false;
        }
        return true;
    }

    @Override
    public void doDeathEffect() {
        this.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(), 0.5D, 0.5D, 0.5D);
        this.discard();
    }
}
