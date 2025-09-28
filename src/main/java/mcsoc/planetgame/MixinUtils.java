package mcsoc.planetgame;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class MixinUtils {
    public static void applyFlippedPassengerOffset(Entity vehicle, CallbackInfoReturnable<Vec3d> cir) {
        Vec3d fixed_offset = cir.getReturnValue().multiply(-1);
        if (vehicle.getFirstPassenger() instanceof PlayerEntity) {
            fixed_offset = fixed_offset.add(0, 1, 0);
        } 

        // Vec3d fixed_offset = new Vec3d(0, 1, 0);
        cir.setReturnValue(fixed_offset);
    }
}
