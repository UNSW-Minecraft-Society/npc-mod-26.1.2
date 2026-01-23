package mcsoc.npcmod.camera;

import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.ClientEntityRegistration;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public interface ClientCameraControlData {

    void setCameraModeRaw(CameraMode mode);

    public default void setCameraMode(CameraMode mode) {
        this.setCameraModeRaw(mode);
        
        if (mode == CameraMode.NORMAL) {
            ClientCameraUtils.detachClientFromCamera();
        }
    }

    public CameraMode getCameraMode();

    public default void setCameraPosition(PositionData pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        if (world == null) return;

        CameraClientEntity camera = new CameraClientEntity(ClientEntityRegistration.CAMERA, world);
        camera.refreshPositionAndAngles(pos.getPos(), pos.yaw(), pos.pitch());
        world.spawnEntity(camera);
        client.setCameraEntity(camera);
    }

    public default PositionData getCameraPosition() {
        Entity camera_entity = MinecraftClient.getInstance().getCameraEntity();
        if (camera_entity == null) camera_entity = MinecraftClient.getInstance().player;
        
        return PositionData.fromEntity(camera_entity);
    }
}
