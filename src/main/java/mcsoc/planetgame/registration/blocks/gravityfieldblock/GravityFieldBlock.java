package mcsoc.planetgame.registration.blocks.gravityfieldblock;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import mcsoc.planetgame.registration.blocks.BlockEntityRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


public class GravityFieldBlock extends BlockWithEntity {
    private static final BooleanProperty POWERED = Properties.POWERED;

    public GravityFieldBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GravityFieldBlockEntity(pos, state, GravityFieldBlockEntity.DEFAULT_SIZE);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(GravityFieldBlock::new);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BlockEntityRegistration.GRAVITY_FIELD_BLOCK_ENTITY, GravityFieldBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL; // Necessary for rendering custom models
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
    }


    // below for rock pile
    
    // @Override
    // public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
    //     return VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875);
    // }

    // @Override
    // public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
    //     return VoxelShapes.union(
    //         VoxelShapes.cuboid(0.3125, 0, 0.3125, 0.6875, 0.012499999999999997, 0.6875),
    //         VoxelShapes.cuboid(0.46875, 0.015625, 0.5, 0.59375, 0.140625, 0.625),
    //         VoxelShapes.cuboid(0.53125, 0.015625, 0.375, 0.65625, 0.140625, 0.5),
    //         VoxelShapes.cuboid(0.40625, 0.015625, 0.34375, 0.53125, 0.140625, 0.46875),
    //         VoxelShapes.cuboid(0.34375, 0.140625, 0.4375, 0.46875, 0.265625, 0.5625),
    //         VoxelShapes.cuboid(0.34375, 0.015625, 0.46875, 0.46875, 0.140625, 0.59375),
    //         VoxelShapes.cuboid(0.46875, 0.140625, 0.4375, 0.59375, 0.265625, 0.5625),
    //         VoxelShapes.cuboid(0.40625, 0.265625, 0.46875, 0.53125, 0.390625, 0.59375),
    //         VoxelShapes.cuboid(0.40625, 0.015625, 0.59375, 0.46875, 0.078125, 0.65625),
    //         VoxelShapes.cuboid(0.46875, 0.390625, 0.46875, 0.53125, 0.453125, 0.53125),
    //         VoxelShapes.cuboid(0.59375, 0.015625, 0.5, 0.65625, 0.078125, 0.5625),
    //         VoxelShapes.cuboid(0.34375, 0.015625, 0.40625, 0.40625, 0.078125, 0.46875),
    //         VoxelShapes.cuboid(0.34375, 0.015625, 0.59375, 0.40625, 0.078125, 0.65625),
    //         VoxelShapes.cuboid(0.46875, 0.140625, 0.34375, 0.53125, 0.203125, 0.40625),
    //         VoxelShapes.cuboid(0.59375, 0.015625, 0.59375, 0.65625, 0.078125, 0.65625)
    //     );
    // }
}
