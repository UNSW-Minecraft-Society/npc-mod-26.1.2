package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.networking.SyncCameraPositionS2CPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;


public class SyncCameraPositionPacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCameraPositionS2CPayload.ID, (payload, context) -> {
            PositionData data = payload.data();
            ClientData.getInstance().setCameraPosition(data);
        });
    }
}