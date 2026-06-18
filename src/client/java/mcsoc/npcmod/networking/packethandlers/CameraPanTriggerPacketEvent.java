package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.networking.CameraPanS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


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