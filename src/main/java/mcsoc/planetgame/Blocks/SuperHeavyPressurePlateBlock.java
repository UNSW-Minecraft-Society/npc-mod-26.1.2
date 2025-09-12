package mcsoc.planetgame.Blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.PlanetGame;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SuperHeavyPressurePlateBlock extends AbstractPressurePlateBlock {

    private double velocity_threshold;
    private static final BooleanProperty POWERED = Properties.POWERED;
    
    public static final MapCodec<SuperHeavyPressurePlateBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
        Codec.DOUBLE.fieldOf("velocity_threshold").forGetter(block -> block.velocity_threshold),
        BlockSetType.CODEC.fieldOf("block_set_type").forGetter((block) -> block.blockSetType), 
        createSettingsCodec()
    ).apply(instance, SuperHeavyPressurePlateBlock::new));


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }


    public SuperHeavyPressurePlateBlock(Double velocity_threshold, BlockSetType type, AbstractBlock.Settings settings) {
        super(settings, type);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
        this.velocity_threshold = velocity_threshold;
    }


    @Override
    protected MapCodec<? extends AbstractPressurePlateBlock> getCodec() {
        return CODEC;
    }


    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {
        Double max_velocity = world.getEntitiesByClass(
            ServerPlayerEntity.class, 
            BOX.offset(pos).offset(0, 2, 0), 
            EntityPredicates.EXCEPT_SPECTATOR.and(entity -> !entity.canAvoidTraps())
        ).stream().map(player -> player.getVelocity().getY()).max(Double::compare).orElse(0D);

        if (Math.abs(max_velocity) > 1E-5) {
            PlanetGame.LOGGER.info("max velocity: {}", max_velocity);
        }
        return max_velocity < -velocity_threshold ? 15 : 0;
    }

    @Override
    protected BlockState setRedstoneOutput(BlockState state, int rsOut) {
        return (BlockState)state.with(POWERED, rsOut > 0);
    }

    @Override
    protected int getRedstoneOutput(BlockState state) {
        return (Boolean)state.get(POWERED) ? 15 : 0;
    }

    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        // TODO implement some method to change velocity threshold
        return super.onUse(state, world, pos, player, hit);
    }

}