package mcsoc.npcmod.camera;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;


public class ClientCameraUtils {

    public static void detachClientFromCamera() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        if (client.getCameraEntity() instanceof CameraClientEntity camera) {
            camera.discard();
        }
        client.setCameraEntity(client.player);
    }

    public static void registerDismountKeybind() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!ClientData.getInstance().getCameraMode().locked() && client.options.keyShift.isDown()) {
                ClientData.getInstance().setCameraMode(CameraMode.NORMAL);
            }
        });
    }
}
