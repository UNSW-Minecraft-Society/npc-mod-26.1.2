package mcsoc.planetgame.registration.blocks.throwswitch;

import com.mojang.serialization.MapCodec;

import mcsoc.planetgame.registration.entities.throwables.ThrowableEntity;
import mcsoc.planetgame.registration.entities.throwables.ThrowableRockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RockSwitch extends ThrowableHittableSwitch {

    protected static final int COOLDOWN_TICKS = 10;

    public RockSwitch(Settings settings) {
        super(settings);
    }

    @Override
    protected int getCooldownTick() {
        return COOLDOWN_TICKS;
    }

    @Override
    public void activateAction(BlockState state, World world, BlockPos pos) {
        state = state.cycle(POWERED);
        world.setBlockState(pos, state, 3);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && getDirection(state) == direction ? 15 : 0;
    }

    @Override
    protected MapCodec<? extends WallMountedBlock> getCodec() {
        return createCodec(RockSwitch::new);
    }

    @Override
    protected void onThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity) {
        if (entity instanceof ThrowableRockEntity) this.activate(state, world, pos);
    }
}
