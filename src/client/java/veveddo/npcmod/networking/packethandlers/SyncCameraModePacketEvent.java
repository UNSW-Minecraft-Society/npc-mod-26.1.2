package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.NpcModClient.ClientData;
import veveddo.npcmod.datatypes.cutscenes.CameraMode;
import veveddo.npcmod.networking.SyncCameraModeS2CPayload;


public class SyncCameraModePacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCameraModeS2CPayload.ID, (payload, context) -> {
            CameraMode mode = payload.mode();
            ClientData.getInstance().setCameraMode(mode);
        });
    }
}