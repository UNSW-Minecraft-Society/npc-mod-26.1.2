package mcsoc.planetgame.blocks.crackedblocks;

import mcsoc.planetgame.entities.throwables.ThrowableEntity;
import mcsoc.planetgame.entities.throwables.ThrowableInteractible;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CrackedBlock extends ThrowableInteractible {

    private void onThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        world.breakBlock(pos, false, entity);
        // Block.replace(state, Blocks.AIR.getDefaultState(), world, pos, 0);
    }

    public default void triggerThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        this.onThrowableCollision(state, world, pos, entity);
    }
}
