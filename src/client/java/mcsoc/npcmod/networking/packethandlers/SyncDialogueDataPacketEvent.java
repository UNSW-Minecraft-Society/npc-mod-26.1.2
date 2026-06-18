package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.datatypes.npcs.DialogueData;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.SyncDialogueDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SyncDialogueDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncDialogueDataS2CPayload.ID, (payload, context) -> {
            DialogueData data = payload.data();
            NPCClientDataLoader.getInstance().registerDialogueData(payload.id(), data.display_name(), data.dialogue(), data.voice());
        });
    }
}