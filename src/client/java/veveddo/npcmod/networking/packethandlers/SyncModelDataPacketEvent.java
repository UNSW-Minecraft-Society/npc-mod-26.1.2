package veveddo.npcmod.networking.packethandlers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;
import veveddo.npcmod.networking.SyncModelDataS2CPayload;

public class SyncModelDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncModelDataS2CPayload.ID, (payload, context) -> {
            ModelData data = payload.data();
            NPCClientDataLoader.getInstance().registerModelData(payload.id(), data.texture(), data.is_slim());
        });
    }
}