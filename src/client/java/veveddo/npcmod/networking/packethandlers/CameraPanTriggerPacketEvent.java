package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.NpcModClient.ClientData;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.networking.CameraPanS2CPayload;


public class CameraPanTriggerPacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(CameraPanS2CPayload.ID, (payload, context) -> {
            PositionData from = payload.from();
            PositionData to = payload.to();
            int ticks = payload.ticks();
            ClientData.getInstance().panCamera(from, to, ticks);
        });
    }
}