package mcsoc.planetgame.mixin.client;

import mcsoc.planetgame.MixinUtils;
import mcsoc.planetgame.PlanetGameClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    
    @Override
    protected void getPassengerAttachmentPosWhenFlipped(CallbackInfoReturnable<Vec3d> cir) {
        if (PlanetGameClient.getPlayerState().grav_dir().equals(Direction.UP)) {
            MixinUtils.applyFlippedPassengerOffset((PlayerEntity)(Object)this, cir);
        }
    }
}
