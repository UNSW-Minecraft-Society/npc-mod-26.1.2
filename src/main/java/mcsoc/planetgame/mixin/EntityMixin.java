package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "getPose", at = @At("RETURN"), cancellable = true)
    protected void getPoseWhenLiftedByPlayer(CallbackInfoReturnable<EntityPose> cir) {

    }

    @Inject(method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
    protected void setVelocityWhenFlipped(Vec3d velocity, CallbackInfo ci) {

    }

    @Inject(method = "getRotationVector", at = @At("RETURN"), cancellable = true)
    protected void getRotationVectorFlipped(CallbackInfoReturnable<Vec3d> cir) {

    }

    @Inject(method = "getPassengerAttachmentPos", at = @At("RETURN"), cancellable = true)
    protected void getPassengerAttachmentPosWhenFlipped(CallbackInfoReturnable<Vec3d> cir) {
        
    }


    // below functions taken from https://github.com/ForwarD-NerN/PlayerLadder (1.19.2 branch)
    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void onRemovePassenger(Entity passenger, CallbackInfo callbackInfo)
    {
        Entity entity = (Entity) (Object) this;

        if(!entity.getWorld().isClient && entity instanceof PlayerEntity)
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
    }

    @Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("TAIL"))
    private void onStartRiding(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir)
    {
        if(!entity.getWorld().isClient && entity instanceof PlayerEntity)
            ((ServerPlayerEntity)entity).networkHandler.sendPacket(new EntityPassengersSetS2CPacket(entity));
    }
}
