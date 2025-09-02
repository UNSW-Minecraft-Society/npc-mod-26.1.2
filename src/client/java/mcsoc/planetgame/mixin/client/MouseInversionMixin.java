package mcsoc.planetgame.mixin.client;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mcsoc.planetgame.PlanetGameClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.Direction;

@Mixin(Mouse.class)
public class MouseInversionMixin {

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @Inject(
        method = "onCursorPos",
        at = @At(
            value = "FIELD", 
            opcode = Opcodes.PUTFIELD, 
            target = "Lnet/minecraft/client/Mouse;cursorDeltaX:D",
            shift = Shift.AFTER
        )
    )
    private void invertXPanning(CallbackInfo ci) {
        if (PlanetGameClient.getPlayerState().grav_dir() == Direction.UP) {
            this.cursorDeltaX *= -1;
        }
    }

    @Inject(
        method = "onCursorPos",
        at = @At(
            value = "FIELD", 
            opcode = Opcodes.PUTFIELD, 
            target = "Lnet/minecraft/client/Mouse;cursorDeltaY:D",
            shift = Shift.AFTER
        )
    )
    private void invertYPanning(CallbackInfo ci) {
        if (PlanetGameClient.getPlayerState().grav_dir() == Direction.UP) {
            this.cursorDeltaY *= -1;
        }
    }
}
