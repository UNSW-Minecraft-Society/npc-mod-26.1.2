package mcsoc.npcmod.camera;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;


public class ClientCameraUtils {

    public static void detachClientFromCamera() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (client.cameraEntity instanceof CameraClientEntity camera) {
            camera.discard();
        }
        client.setCameraEntity(client.player);
    }

    public static void registerDismountKeybind() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!ClientData.getInstance().getCameraMode().locked() && client.options.sneakKey.isPressed()) {
                ClientData.getInstance().setCameraMode(CameraMode.NORMAL);
            }
        });
    }
}
