package mcsoc.npcmod.dataloader.datastorage.npc;

import java.util.List;
import java.util.Map;

import mcsoc.npcmod.datatypes.npcs.DialogueData;
import mcsoc.npcmod.datatypes.npcs.ModelData;
import mcsoc.npcmod.datatypes.npcs.NPCData;
import mcsoc.npcmod.datatypes.npcs.NPCMode;
import mcsoc.npcmod.entities.npc.BaseNPC;
import mcsoc.npcmod.entities.npc.BasicNPC;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public interface NPCDataStorage {

    static final ModelData MODEL_NOT_FOUND = new ModelData(ResourceLocation.withDefaultNamespace("textures/entity/player/wide/noor.png"), true);
    static final DialogueData DIALOGUE_NOT_FOUND = new DialogueData(Component.literal("DEFAULT"), List.of(Component.literal("Dialogue not found.")), ResourceLocation.withDefaultNamespace("music_disc.far"));
    static final String MISSING_KEY = "mssngn";
    static final NPCData NPC_NOT_FOUND = new NPCData(MISSING_KEY, MISSING_KEY, NPCMode.BACKGROUND);


    abstract Map<String, NPCData> getNPCMap();
    abstract Map<String, ModelData> getModelMap();
    abstract Map<String, DialogueData> getDialogueMap();
    abstract NPCData getNPCData(BaseNPC npc);

    default NPCData getBasicNPCData(BasicNPC npc) {
        return this.getNPCMap().getOrDefault(npc.getID(), NPC_NOT_FOUND);
    }

    public default DialogueData getDialogue(BaseNPC npc) {
        NPCData npc_data = this.getNPCData(npc);
        return this.getDialogueMap().getOrDefault(npc_data.dialogue_id(), DIALOGUE_NOT_FOUND);
    }
    public default ModelData getModel(BaseNPC npc) {
        NPCData npc_data = this.getNPCData(npc);
        return this.getModelMap().getOrDefault(npc_data.model_id(), MODEL_NOT_FOUND);
    }

    public default void registerModelData(String id, ResourceLocation texture, boolean is_slim) {
        this.getModelMap().put(id, new ModelData(texture, is_slim));
    }
    public default void registerDialogueData(String id, Component display_name, List<Component> dialogue, ResourceLocation voice) {
        this.getDialogueMap().put(id, new DialogueData(display_name, dialogue, voice));
    }
    public default void registerBackgroundNPC(String id, String model_id, String dialogue_id) {
        this.getNPCMap().put(id, new NPCData(model_id, dialogue_id, NPCMode.BACKGROUND));
    }
    public default void registerStoryNPC(String id, String model_id, String dialogue_id) {
        this.getNPCMap().put(id, new NPCData(model_id, dialogue_id, NPCMode.MAIN));
    }
}
