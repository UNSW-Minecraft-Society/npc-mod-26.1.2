package mcsoc.npcmod.dataloader;

import mcsoc.npcmod.NPCMod;
import mcsoc.npcmod.networking.SyncDialogueDataS2CPayload;
import mcsoc.npcmod.networking.SyncModelDataS2CPayload;
import mcsoc.npcmod.networking.SyncNPCDataS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class NPCServerDataLoader extends NPCDataStorage {

    private static final NPCServerDataLoader INSTANCE = new NPCServerDataLoader();

    private static final String MODEL_DATA_PATH = NPCMod.MOD_ID + "/model_data.json";
    private static final String DIALOGUE_DATA_PATH = NPCMod.MOD_ID + "/dialogue_data.json";
    private static final String NPC_DATA_PATH = NPCMod.MOD_ID + "/npc_data.json";

    private final NPCJsonDataParser parser = NPCJsonDataParser.getInstance();

    private NPCServerDataLoader() {
        this.loadData();
        this.saveData();
    }
    public static NPCServerDataLoader getInstance() {
        return INSTANCE;
    }

    public void loadData() {
        this.getModelMap().putAll(parser.loadModelData(MODEL_DATA_PATH));
        this.getDialogueMap().putAll(parser.loadDialogueData(DIALOGUE_DATA_PATH));
        this.getNPCMap().putAll(parser.loadNPCData(NPC_DATA_PATH));      
    }
    public void saveData() {
        parser.saveModelData(MODEL_DATA_PATH, this.getModelMap());
        parser.saveDialogueData(DIALOGUE_DATA_PATH, this.getDialogueMap());
        parser.saveNPCData(NPC_DATA_PATH, this.getNPCMap());
    }

    public void syncClientData(MinecraftServer server, ServerPlayerEntity player) {
        NPCMod.LOGGER.info("sending packet to {}", player.getDisplayName());
        NPCMod.LOGGER.info("map len {}", this.getDialogueMap().size());
        this.getModelMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncModelDataS2CPayload(id, data))
        );
        this.getDialogueMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncDialogueDataS2CPayload(id, data))
        );
        this.getNPCMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncNPCDataS2CPayload(id, data))
        );
    }
}
