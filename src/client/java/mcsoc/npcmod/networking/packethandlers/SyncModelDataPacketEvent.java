package mcsoc.npcmod.networking.packethandlers;

import mcsoc.npcmod.datatypes.npcs.ModelData;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.SyncModelDataS2CPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class SyncModelDataPacketEvent {

    public static void registerHandler() {
        ClientPlayNetworking.registerGlobalReceiver(SyncModelDataS2CPayload.ID, (payload, context) -> {
            ModelData data = payload.data();
            NPCClientDataLoader.getInstance().registerModelData(payload.id(), data.uuid());
        });
    }
}