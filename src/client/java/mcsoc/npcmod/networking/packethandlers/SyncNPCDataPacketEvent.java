package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.SyncNPCDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SyncNPCDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncNPCDataS2CPayload.ID, (payload, context) -> {
            NPCData data = payload.data();
            switch (data.mode()) {
                case BACKGROUND:
                    NPCClientDataLoader.getInstance().registerBackgroundNPC(payload.id(), data.model_id(), data.dialogue_id());
                    break;
                case MAIN:
                    NPCClientDataLoader.getInstance().registerStoryNPC(payload.id(), data.model_id(), data.dialogue_id());
                    break;
                default:
                    break;
            }
        });
    }
}