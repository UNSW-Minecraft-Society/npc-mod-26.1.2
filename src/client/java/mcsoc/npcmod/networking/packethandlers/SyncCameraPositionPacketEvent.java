package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.NpcModClient.ClientData;
import mcsoc.npcmod.datatypes.cutscenes.PositionData;
import mcsoc.npcmod.networking.SyncCameraPositionS2CPayload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;


public class SyncCameraPositionPacketEvent {
    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCameraPositionS2CPayload.ID, (payload, context) -> {
            PositionData data = payload.data();
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            ClientData.getInstance().setCameraPosition(data.addPos(player.getCameraPosVec(0)));
        });
    }
}