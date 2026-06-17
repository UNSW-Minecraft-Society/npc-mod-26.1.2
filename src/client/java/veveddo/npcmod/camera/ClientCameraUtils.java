package veveddo.npcmod.camera;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import veveddo.npcmod.NpcModClient.ClientData;
import veveddo.npcmod.datatypes.cutscenes.CameraMode;
import veveddo.npcmod.entities.camera.CameraClientEntity;


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
