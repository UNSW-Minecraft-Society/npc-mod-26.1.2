package mcsoc.planetgame.mixin.client;

import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import mcsoc.planetgame.PlanetGameClient;
import net.minecraft.client.render.Camera;

@Mixin(value = Camera.class, priority = 1003)
public class CameraRotationMixin {

    @Shadow @Final
    public Quaternionf rotation;

    @ModifyArg(method = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", 
                at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;"),
                index = 2)
    private float rotZ90(float angleZ) {
        // PlanetGameClient.LOGGER.info("hello");
        float rot = switch (PlanetGameClient.getPlayerState().getPlayerGravDirection()) {
            case UP -> 3.1415927F;
            default -> 0F;
        };

        // PlanetGameClient.LOGGER.info("dir: {}, rot: {}", PlanetGameClient.getPlayerState().getPlayerGravDirection(), angleZ + rot);

        return angleZ + rot;
    }
}