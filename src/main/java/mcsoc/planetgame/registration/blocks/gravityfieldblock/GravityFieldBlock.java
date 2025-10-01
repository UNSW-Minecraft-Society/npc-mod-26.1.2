package mcsoc.planetgame.registration.blocks.gravityfieldblock;

import org.jetbrains.annotations.Nullable;

import mcsoc.planetgame.registration.blocks.BlockEntityRegistration;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class GravityFieldBlock extends BlockWithEntity {
    private static final BooleanProperty POWERED = Properties.POWERED;
    
    private final float radius;

    public GravityFieldBlock(Settings settings, float radius) {
        super(settings);
        this.radius = radius;
    }

    public GravityFieldBlock(Settings settings) {
        this(settings, GravityFieldBlockEntity.DEFAULT_SIZE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GravityFieldBlockEntity(pos, state, this.radius);
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
}
