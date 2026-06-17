package veveddo.npcmod.dataloader.datastorage;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

import veveddo.npcmod.NpcMod;
import veveddo.npcmod.dataloader.datastorage.cutscenes.CutsceneDataStorage;
import veveddo.npcmod.dataloader.datastorage.npc.MovingNPCDataStorage;
import veveddo.npcmod.dataloader.datastorage.npc.NPCDataStorage;
import veveddo.npcmod.dataloader.jsonparser.NpcModJsonDataLoader;
import veveddo.npcmod.datatypes.cutscenes.CutsceneData;
import veveddo.npcmod.datatypes.npcs.DialogueData;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.datatypes.npcs.MovementData;
import veveddo.npcmod.datatypes.npcs.MovingNPCData;
import veveddo.npcmod.datatypes.npcs.NPCData;
import veveddo.npcmod.entities.npc.BaseNPC;
import veveddo.npcmod.entities.npc.BasicNPC;
import veveddo.npcmod.entities.npc.MovingNPC;
import veveddo.npcmod.networking.SyncDialogueDataS2CPayload;
import veveddo.npcmod.networking.SyncModelDataS2CPayload;
import veveddo.npcmod.networking.SyncMovingNPCDataS2CPayload;
import veveddo.npcmod.networking.SyncNPCDataS2CPayload;


public class NpcModServerDataStorage implements NPCDataStorage, MovingNPCDataStorage, CutsceneDataStorage {

    private static final NpcModServerDataStorage INSTANCE = new NpcModServerDataStorage();
    private boolean has_updated = false;

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();
    private static final String MODEL_DATA_PATH = NpcMod.MOD_ID + "/model_data.json";
    private static final String DIALOGUE_DATA_PATH = NpcMod.MOD_ID + "/dialogue_data.json";
    private static final String NPC_DATA_PATH = NpcMod.MOD_ID + "/npc_data.json";

    private final Map<String, MovingNPCData> moving_npc_data_map = new HashMap<>();
    private final Map<String, MovementData> movement_data_map = new HashMap<>();
    private static final String MOVEMENT_DATA_PATH = NpcMod.MOD_ID + "/movement_data.json";
    private static final String MOVING_NPC_DATA_PATH = NpcMod.MOD_ID + "/moving_npc_data.json";

    private final Map<String, CutsceneData> cutscene_data_map = new HashMap<>();
    private static final String CUTSCENE_DATA_PATH = NpcMod.MOD_ID + "/cutscene_data.json";

    private final NpcModJsonDataLoader parser = NpcModJsonDataLoader.getInstance();

    private NpcModServerDataStorage() {
        this.loadData();
        this.saveData();
    }
    public static NpcModServerDataStorage getInstance() {
        return INSTANCE;
    }
    public boolean hasUpdated() {
        return this.has_updated;
    }
    public void tick() {
        if (this.has_updated) this.has_updated = false;
    }

    @Override
    public Map<String, DialogueData> getDialogueMap() {
        return dialogue_data_map;
    }
    @Override
    public Map<String, ModelData> getModelMap() {
        return model_data_map;
    }
    @Override
    public Map<String, NPCData> getNPCMap() {
        return npc_data_map;
    }

    @Override
    public Map<String, MovingNPCData> getMovingNPCMap() {
        return moving_npc_data_map;
    }
    @Override
    public Map<String, MovementData> getMovementMap() {
        return movement_data_map;
    }

    @Override
    public Map<String, CutsceneData> getCutsceneMap() {
        return cutscene_data_map;
    }


    @Override
    public NPCData getNPCData(BaseNPC npc) {
        return switch (npc) {
            case BasicNPC basic_npc -> this.getBasicNPCData(basic_npc);
            case MovingNPC moving_npc -> this.getMovingNPCData(moving_npc).base_data();
            default -> NPC_NOT_FOUND;
        };
    }

    public void loadData() {
        this.getModelMap().putAll(parser.loadModelData(MODEL_DATA_PATH));
        this.getDialogueMap().putAll(parser.loadDialogueData(DIALOGUE_DATA_PATH));
        this.getNPCMap().putAll(parser.loadNPCData(NPC_DATA_PATH));

        this.getMovementMap().putAll(parser.loadMovementData(MOVEMENT_DATA_PATH));
        this.getMovingNPCMap().putAll(parser.loadMovingNPCData(MOVING_NPC_DATA_PATH));

        this.getCutsceneMap().putAll(parser.loadCutsceneData(CUTSCENE_DATA_PATH));
    }
    public void saveData() {
        parser.saveModelData(MODEL_DATA_PATH, this.getModelMap());
        parser.saveDialogueData(DIALOGUE_DATA_PATH, this.getDialogueMap());
        parser.saveNPCData(NPC_DATA_PATH, this.getNPCMap());

        parser.saveMovementData(MOVEMENT_DATA_PATH, this.getMovementMap());
        parser.saveMovingNPCData(MOVING_NPC_DATA_PATH, this.getMovingNPCMap());
        
        parser.saveCutsceneData(CUTSCENE_DATA_PATH, this.getCutsceneMap());
    }

    public void syncClientData(ServerPlayer player) {
        this.getModelMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncModelDataS2CPayload(id, data))
        );
        this.getDialogueMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncDialogueDataS2CPayload(id, data))
        );
        this.getNPCMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncNPCDataS2CPayload(id, data))
        );
        this.getMovingNPCMap().forEach((id, data) -> 
            ServerPlayNetworking.send(player, new SyncMovingNPCDataS2CPayload(id, data.base_data()))
        );

        this.has_updated = true;
    }
}
