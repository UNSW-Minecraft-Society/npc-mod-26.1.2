package mcsoc.planetgame.registration.entities.throwables;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ThrowableInteractible {

    public void triggerThrowableCollision(BlockState state, World world, BlockPos pos, ThrowableEntity entity);
}
