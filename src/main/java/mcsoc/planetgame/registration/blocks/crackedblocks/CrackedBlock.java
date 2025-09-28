package mcsoc.planetgame.registration.blocks.crackedblocks;

import mcsoc.planetgame.registration.entities.throwables.ThrowableEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class CrackedBlock extends Block {
    protected CrackedBlock(Settings settings) {
        super(settings);
    }

    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 15.0, 15.0, 15.0);

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    protected void onThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        Block.replace(state, Blocks.AIR.getDefaultState(), world, pos, 0);
    }

    public void triggerThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        this.onThrowableCollision(state, world, pos, entity);
    }
}
