package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.NpcModClient.ClientData;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.networking.SyncCameraPositionS2CPayload;


public class SyncCameraPositionPacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCameraPositionS2CPayload.ID, (payload, context) -> {
            PositionData data = payload.data();
            ClientData.getInstance().setCameraPosition(data);
        });
    }
}