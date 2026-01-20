package mcsoc.npcmod.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public class EntityCameraMixin {

    @Inject(method = "getCameraPosVec", at = @At("RETURN"), cancellable = true)
    protected void modifyCameraPosition(CallbackInfoReturnable<Vec3d> cir) {
        
    }
}
