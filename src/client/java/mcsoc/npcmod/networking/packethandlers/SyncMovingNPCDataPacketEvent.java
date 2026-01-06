package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.SyncMovingNPCDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SyncMovingNPCDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncMovingNPCDataS2CPayload.ID, (payload, context) -> {
            NPCData data = payload.data();
            NPCClientDataLoader.getInstance().registerMovingNPC(payload.id(), data.model_id(), data.dialogue_id(), null);
        });
    }
}