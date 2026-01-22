package mcsoc.npcmod.camera;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity.RemovalReason;


public class ClientCameraUtils {

    public static void detachClientFromCamera() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (client.cameraEntity instanceof CameraClientEntity camera) {
            client.setCameraEntity(client.player);
            
            client.gameRenderer.getCamera().update(
                client.world, 
                client.player,
                client.options.getPerspective().isFirstPerson(), 
                false, 
                1.0F
            );
            client.gameRenderer.onCameraEntitySet(client.player);

            client.world.removeEntity(camera.getId(), RemovalReason.DISCARDED);
            camera.discard();
        }
    }

    public static void registerDismountKeybind() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ClientData.getInstance().getCameraMode() == CameraMode.UNLOCKED && client.player.input.sneaking) {
                detachClientFromCamera();
                ClientData.getInstance().setCameraMode(CameraMode.NORMAL);
            }
        });
    }
}
