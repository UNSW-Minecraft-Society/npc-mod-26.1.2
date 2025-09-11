package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker extends EntityInvoker {

    @Invoker("getJumpVelocity")
    public float invokeGetJumpVelocity();
}
