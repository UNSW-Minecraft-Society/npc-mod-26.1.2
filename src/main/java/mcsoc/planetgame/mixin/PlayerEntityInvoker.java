package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public interface PlayerEntityInvoker extends LivingEntityInvoker {

}
