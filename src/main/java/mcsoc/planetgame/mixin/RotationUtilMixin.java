package mcsoc.planetgame.mixin;

import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import gravity_changer.util.RotationUtil;
import net.minecraft.util.math.Direction;

@Mixin(value = RotationUtil.class, remap = false)
public class RotationUtilMixin {

    @Shadow @Final
    private static Quaternionf[] WORLD_ROTATION_QUATERNIONS;

    @Shadow @Final
    private static Quaternionf[] ENTITY_ROTATION_QUATERNIONS;

    @Inject(
        method = "<clinit>", 
        at = @At("TAIL"),
        cancellable = true
    )
    private static void undoUpGravRot(CallbackInfo ci) {
        // WORLD_ROTATION_QUATERNIONS[Direction.UP.getId()] = WORLD_ROTATION_QUATERNIONS[Direction.DOWN.getId()].rotationYXZ(0, 0, 3.1415926536F);
        // ENTITY_ROTATION_QUATERNIONS[Direction.UP.getId()] = ENTITY_ROTATION_QUATERNIONS[Direction.DOWN.getId()].rotationYXZ(0, 0, 3.1415926536F);
    }

    // @Inject(method = "getWorldRotationQuaternion", at = @At("RETURN"))
    // private static void printWorldRotation(CallbackInfoReturnable<Quaternionf> cir) {
    //     PlanetGame.LOGGER.info("get rot: {}", cir.getReturnValue());
    // }

    // @Inject(method = "getCameraRotationQuaternion", at = @At("RETURN"))
    // private static void printCameraRotation(CallbackInfoReturnable<Quaternionf> cir) {
    //     PlanetGame.LOGGER.info("get rot: {}", cir.getReturnValue());
    // }
}