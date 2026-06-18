package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.networking.SyncCameraModeS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class SyncCameraModePacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCameraModeS2CPayload.ID, (payload, context) -> {
            CameraMode mode = payload.mode();
            ClientData.getInstance().setCameraMode(mode);
        });
    }
}