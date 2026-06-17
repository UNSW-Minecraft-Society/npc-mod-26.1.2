package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.datatypes.npcs.DialogueData;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;
import veveddo.npcmod.networking.SyncDialogueDataS2CPayload;

public class SyncDialogueDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncDialogueDataS2CPayload.ID, (payload, context) -> {
            DialogueData data = payload.data();
            NPCClientDataLoader.getInstance().registerDialogueData(payload.id(), data.display_name(), data.dialogue(), data.voice());
        });
    }
}