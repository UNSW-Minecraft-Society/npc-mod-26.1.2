package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mcsoc.planetgame.PlanetGame;
import net.minecraft.entity.EntityPose;
import net.minecraft.server.network.ServerPlayerEntity;


@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin {
    
    @Override
    protected void getPoseWhenLiftedByPlayer(CallbackInfoReturnable<EntityPose> cir) {
        if (((ServerPlayerEntity)(Object) this).getVehicle() instanceof ServerPlayerEntity p2) {
            PlanetGame.LOGGER.info("{} riding {}, setting to swim", (ServerPlayerEntity)(Object) this, p2);
            cir.setReturnValue(EntityPose.SWIMMING);
        }
    }

}