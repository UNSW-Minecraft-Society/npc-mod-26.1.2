package mcsoc.planetgame.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import mcsoc.planetgame.PlanetGameClient;
import net.minecraft.client.render.Camera;

@Mixin(Camera.class)
public class CameraRotationMixin {
    // @Inject(method = "update", at = @At(value = "HEAD"))
    // private void beforeUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        
    // }
    @ModifyArg(method = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", 
                at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"),
                index = 2)
    private float rotZ90(float angleZ) {
        PlanetGameClient.LOGGER.info("hello");
        return angleZ + 3.1415927F;
    }
}