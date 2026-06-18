package mcsoc.npcmod.camera;

import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.ClientEntityRegistration;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;


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
        Minecraft client = Minecraft.getInstance();
        ClientLevel world = client.level;
        if (world == null) return;

        CameraClientEntity camera = new CameraClientEntity(ClientEntityRegistration.CAMERA, world);
        camera.moveTo(pos.getPos(), pos.yaw(), pos.pitch());
        world.addFreshEntity(camera);
        client.setCameraEntity(camera);
    }

    public default PositionData getCameraPosition() {
        Entity camera_entity = Minecraft.getInstance().getCameraEntity();
        if (camera_entity == null) camera_entity = Minecraft.getInstance().player;
        
        return PositionData.fromEntity(camera_entity);
    }
}
