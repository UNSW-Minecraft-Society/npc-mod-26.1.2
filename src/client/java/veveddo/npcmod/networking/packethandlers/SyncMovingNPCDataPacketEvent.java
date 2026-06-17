package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.datatypes.npcs.NPCData;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;
import veveddo.npcmod.networking.SyncMovingNPCDataS2CPayload;

public class SyncMovingNPCDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncMovingNPCDataS2CPayload.ID, (payload, context) -> {
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