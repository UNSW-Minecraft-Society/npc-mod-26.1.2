package mcsoc.planetgame.registration.blocks.throwswitch;

import mcsoc.planetgame.registration.entities.throwables.ThrowableEntity;
import mcsoc.planetgame.registration.entities.throwables.ThrowableInteractible;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class ThrowableHittableSwitch extends WallMountedBlock implements ThrowableInteractible {
    
    protected static final BooleanProperty POWERED = Properties.POWERED;
    protected static final VoxelShape NORTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 10.0, 11.0, 12.0, 16.0);
    protected static final VoxelShape SOUTH_WALL_SHAPE = Block.createCuboidShape(5.0, 4.0, 0.0, 11.0, 12.0, 6.0);
    protected static final VoxelShape WEST_WALL_SHAPE = Block.createCuboidShape(10.0, 4.0, 5.0, 16.0, 12.0, 11.0);
    protected static final VoxelShape EAST_WALL_SHAPE = Block.createCuboidShape(0.0, 4.0, 5.0, 6.0, 12.0, 11.0);
    protected static final VoxelShape FLOOR_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 0.0, 4.0, 11.0, 6.0, 12.0);
    protected static final VoxelShape FLOOR_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 0.0, 5.0, 12.0, 6.0, 11.0);
    protected static final VoxelShape CEILING_Z_AXIS_SHAPE = Block.createCuboidShape(5.0, 10.0, 4.0, 11.0, 16.0, 12.0);
    protected static final VoxelShape CEILING_X_AXIS_SHAPE = Block.createCuboidShape(4.0, 10.0, 5.0, 12.0, 16.0, 11.0);

    protected boolean can_activate = true;

    protected ThrowableHittableSwitch(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(POWERED, false).with(FACE, BlockFace.WALL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
      builder.add(FACE, FACING, POWERED);
   }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }


    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACE)) {
            case CEILING -> 
                switch (state.get(FACING)) {
                    case NORTH, SOUTH -> 
                        CEILING_X_AXIS_SHAPE;
                    case EAST, WEST ->
                        CEILING_Z_AXIS_SHAPE;
                    default -> VoxelShapes.empty();
                };
            case FLOOR ->
                switch (state.get(FACING)) {
                    case NORTH, SOUTH ->
                        FLOOR_X_AXIS_SHAPE;
                    case EAST, WEST ->
                        FLOOR_Z_AXIS_SHAPE;
                    default -> VoxelShapes.empty();
                };
            case WALL ->
                switch (state.get(FACING)) {
                    case NORTH ->
                        NORTH_WALL_SHAPE;
                    case SOUTH ->
                        SOUTH_WALL_SHAPE;
                    case EAST ->
                        EAST_WALL_SHAPE;
                    case WEST ->
                        WEST_WALL_SHAPE;
                    default -> VoxelShapes.empty();
                };
        };
    }

    protected abstract int getCooldownTick();

    protected abstract void onThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity);
    
    public void triggerThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        this.onThrowableCollision(state, world, pos, entity);
    }

    protected void allowActivation() {
        this.can_activate = true;
    }

    protected abstract void activateAction(BlockState state, World world, BlockPos pos);

    protected void activate(BlockState state, World world, BlockPos pos) {
        if (!this.can_activate) return;
        
        this.can_activate = false;
        world.scheduleBlockTick(pos, this, this.getCooldownTick());
        activateAction(state, world, pos);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.allowActivation();
    }
}
