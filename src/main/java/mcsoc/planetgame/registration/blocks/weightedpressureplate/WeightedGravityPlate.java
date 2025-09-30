package mcsoc.planetgame.registration.blocks.weightedpressureplate;

import com.mojang.serialization.MapCodec;
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

public class WeightedGravityPlate extends AbstractPressurePlateBlock {

    private double velocity_threshold;
    private static final BooleanProperty POWERED = Properties.POWERED;
    private static final double DEFAULT_VELOCITY_THRESHOLD = 1.5F;


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }


    public WeightedGravityPlate(AbstractBlock.Settings settings, Double velocity_threshold, BlockSetType type) {
        super(settings, type);
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
        this.velocity_threshold = velocity_threshold;
    }

    public WeightedGravityPlate(AbstractBlock.Settings settings) {
        this(settings, DEFAULT_VELOCITY_THRESHOLD, BlockSetType.IRON);
    }


    @Override
    protected MapCodec<? extends AbstractPressurePlateBlock> getCodec() {
        return createCodec(WeightedGravityPlate::new);
    }


    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {
        double max_velocity = world.getEntitiesByClass(
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
        return state.with(POWERED, rsOut > 0);
    }

    @Override
    protected int getRedstoneOutput(BlockState state) {
        return state.get(POWERED) ? 15 : 0;
    }

    
    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        
        // TODO implement some method to change velocity threshold
        return super.onUse(state, world, pos, player, hit);
    }

}