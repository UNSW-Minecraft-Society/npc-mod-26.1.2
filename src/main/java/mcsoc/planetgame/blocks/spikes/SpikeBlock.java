package mcsoc.planetgame.blocks.spikes;

import mcsoc.planetgame.entities.damagesources.DamageSourceRegistration;
import mcsoc.planetgame.statemanagement.GameStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SpikeBlock extends PointedDripstoneBlock {

    public SpikeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (state.get(THICKNESS) == Thickness.TIP && entity instanceof ServerPlayerEntity player && (
            (GameStateManager.getPlayerGravityDirection(player).equals(Direction.DOWN) && state.get(VERTICAL_DIRECTION) == Direction.UP) ||
            (GameStateManager.getPlayerGravityDirection(player).equals(Direction.UP) && state.get(VERTICAL_DIRECTION) == Direction.DOWN)
        )) {
            entity.handleFallDamage(100, 1, DamageSourceRegistration.of(world, DamageSourceRegistration.SPIKE_DAMAGE_TYPE));
        } else {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        }
    }
}
