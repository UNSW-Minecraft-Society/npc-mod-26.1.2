package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

}