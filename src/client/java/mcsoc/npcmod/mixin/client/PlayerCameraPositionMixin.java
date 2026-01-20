package mcsoc.npcmod.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public class PlayerCameraPositionMixin extends EntityCameraMixin {

    @Override
    protected void modifyCameraPosition(CallbackInfoReturnable<Vec3d> cir) {
        ClientData client_data = ClientData.getInstance();
        switch (client_data.getCameraMode()) {
            case UNLOCKED:
                client_data.setCameraMode(CameraMode.NORMAL);
            case LOCKED:
                cir.setReturnValue(client_data.getCameraPosition().getPos());
                break;
            case NORMAL:
            default:
        }
    }
}
