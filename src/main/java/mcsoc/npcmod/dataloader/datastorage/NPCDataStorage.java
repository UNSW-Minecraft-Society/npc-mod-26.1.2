package mcsoc.npcmod.dataloader.datastorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mcsoc.npcmod.dataloader.datastorage.datatypes.DialogueData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.Mode;
import mcsoc.npcmod.dataloader.datastorage.datatypes.ModelData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementInstruction;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovingNPCData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;
import mcsoc.npcmod.entities.npc.BaseNPC;
import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.MovingNPC;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public abstract class NPCDataStorage {
    private static final ModelData MODEL_NOT_FOUND = new ModelData(UUID.fromString("e2d03233-e72a-4389-9b41-12a9ff98bf3d"));
    private static final DialogueData DIALOGUE_NOT_FOUND = new DialogueData(Text.of("DEFAULT"), List.of(Text.of("Dialogue not found.")), Identifier.ofVanilla("music_disc.far"));
    private static final MovementData MOVEMENTS_NOT_FOUND = new MovementData(List.of(new MovementInstruction.Jump()));
    private static final String MISSING_KEY = "mssngn";
    private static final NPCData NPC_NOT_FOUND = new NPCData(MISSING_KEY, MISSING_KEY, Mode.BACKGROUND);
    private static final MovingNPCData MOVING_NPC_NOT_FOUND = new MovingNPCData(NPC_NOT_FOUND, MISSING_KEY);

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    private final Map<String, MovingNPCData> moving_npc_data_map = new HashMap<>();
    private final Map<String, MovementData> movement_data_map = new HashMap<>();


    protected Map<String, NPCData> getNPCMap() {
        return this.npc_data_map;
    }
    protected Map<String, ModelData> getModelMap() {
        return this.model_data_map;
    }
    protected Map<String, DialogueData> getDialogueMap() {
        return this.dialogue_data_map;
    }

    protected NPCData getBasicNPCData(BasicNPC npc) {
        return this.getNPCMap().getOrDefault(npc.getID(), NPC_NOT_FOUND);
    }

    protected MovingNPCData getMovingNPCData(MovingNPC npc) {
        return this.getMovingNPCMap().getOrDefault(npc.getID(), MOVING_NPC_NOT_FOUND);
    }

    protected NPCData getNPCData(BaseNPC npc) {
        return switch (npc) {
            case BasicNPC basic_npc -> this.getBasicNPCData(basic_npc);
            case MovingNPC moving_npc -> this.getMovingNPCData(moving_npc).base_data();
            default -> NPC_NOT_FOUND;
        };
    }

    public DialogueData getDialogue(BaseNPC npc) {
        NPCData npc_data = this.getNPCData(npc);
        return this.getDialogueMap().getOrDefault(npc_data.dialogue_id(), DIALOGUE_NOT_FOUND);
    }
    public ModelData getModel(BaseNPC npc) {
        NPCData npc_data = this.getNPCData(npc);
        return this.getModelMap().getOrDefault(npc_data.model_id(), MODEL_NOT_FOUND);
    }

    public void registerModelData(String id, UUID uuid) {
        this.getModelMap().put(id, new ModelData(uuid));
    }
    public void registerDialogueData(String id, Text display_name, List<Text> dialogue, Identifier voice) {
        this.getDialogueMap().put(id, new DialogueData(display_name, dialogue, voice));
    }
    public void registerBackgroundNPC(String id, String model_id, String dialogue_id) {
        this.getNPCMap().put(id, new NPCData(model_id, dialogue_id, Mode.BACKGROUND));
    }
    public void registerStoryNPC(String id, String model_id, String dialogue_id) {
        this.getNPCMap().put(id, new NPCData(model_id, dialogue_id, Mode.MAIN));
    }


    protected Map<String, MovingNPCData> getMovingNPCMap() {
        return this.moving_npc_data_map;
    }
    protected Map<String, MovementData> getMovementMap() {
        return this.movement_data_map;
    }

    public MovementData getMovements(MovingNPC npc) {
        MovingNPCData npc_data = this.getMovingNPCData(npc);
        return this.getMovementMap().getOrDefault(npc_data.movement_id(), MOVEMENTS_NOT_FOUND);
    }
    public void registerMovementData(String id, MovementData movements) {
        this.getMovementMap().put(id, movements);
    }
    public void registerMovingNPC(String id, String model_id, String dialogue_id, String movement_id) {
        this.getMovingNPCMap().put(id, new MovingNPCData(new NPCData(model_id, dialogue_id, Mode.MOVING), movement_id));
    }
}
