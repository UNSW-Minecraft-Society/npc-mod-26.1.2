package mcsoc.planetgame.mixin;

import mcsoc.planetgame.MixinUtils;
import mcsoc.planetgame.statemanagement.gamestate.GameStateManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {
    
    // @Override
    // protected void getPoseWhenLiftedByPlayer(CallbackInfoReturnable<EntityPose> cir) {
    //     if (((ServerPlayerEntity)(Object) this).getVehicle() instanceof ServerPlayerEntity p2) {
    //         cir.setReturnValue(EntityPose.SWIMMING);
    //     }
    // }

    @Override
    protected void getPassengerAttachmentPosWhenFlipped(CallbackInfoReturnable<Vec3d> cir) {
        ServerPlayerEntity this_player = (ServerPlayerEntity)(Object)this;
        if (GameStateManager.getPlayerGravityDirection(this_player).equals(Direction.UP)) {
            MixinUtils.applyFlippedPassengerOffset(this_player, cir);
        }
    }
}