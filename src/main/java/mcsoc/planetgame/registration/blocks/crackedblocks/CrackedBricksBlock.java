package mcsoc.planetgame.registration.blocks.crackedblocks;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.registration.entities.ThrowableRockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TargetBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CrackedBricksBlock extends Block {
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 15.0, 15.0, 15.0);

    public CrackedBricksBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }


    protected void onThrowableRockCollision(BlockState state, World world, BlockPos pos, ThrowableRockEntity entity) {
        PlanetGame.LOGGER.info("I was hit by rock!");
        CrackedBricksBlock.replace(state, Blocks.AIR.getDefaultState(), world, pos, 0);
    }

    public void triggerThrowableRockCollision(BlockState state, World world, BlockPos pos, ThrowableRockEntity entity) {
        this.onThrowableRockCollision(state, world, pos, entity);
    }
}
