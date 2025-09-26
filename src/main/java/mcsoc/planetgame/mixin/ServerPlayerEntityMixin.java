package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.server.network.ServerPlayerEntity;


@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin {
    
    // @Override
    // protected void getPoseWhenLiftedByPlayer(CallbackInfoReturnable<EntityPose> cir) {
    //     if (((ServerPlayerEntity)(Object) this).getVehicle() instanceof ServerPlayerEntity p2) {
    //         cir.setReturnValue(EntityPose.SWIMMING);
    //     }
    // }

}