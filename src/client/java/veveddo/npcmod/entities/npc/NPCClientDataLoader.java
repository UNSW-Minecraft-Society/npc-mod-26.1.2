package veveddo.npcmod.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

import veveddo.npcmod.NpcMod;
import veveddo.npcmod.dataloader.datastorage.npc.NPCDataStorage;
import veveddo.npcmod.datatypes.npcs.DialogueData;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.datatypes.npcs.NPCData;


public class NPCClientDataLoader implements NPCDataStorage {

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    private final Map<Identifier, PlayerSkin> skin_data = new HashMap<>();
    private static final NPCClientDataLoader INSTANCE = new NPCClientDataLoader();

    private NPCClientDataLoader() {}
    public static NPCClientDataLoader getInstance() {
        return INSTANCE;
    }

    public void buildSkin(ModelData model_data) {
        if (this.skin_data.containsKey(model_data.texture())) return;

        Identifier texture_id = model_data.texture();
        try {
            Minecraft client = Minecraft.getInstance();
            client.getTextureManager().getTexture(texture_id.withPath((path) -> "textures/" + path + ".png"));
        } catch (Exception e) {
            NpcMod.LOGGER.error("Failed to bind custom NPC texture: " + texture_id + " -> ", e);
        }

        this.skin_data.put(texture_id, 
            new PlayerSkin(
                new ResourceTexture(model_data.texture()),
                null, null, 
                model_data.is_slim() ? PlayerModelType.SLIM : PlayerModelType.WIDE, 
                true
            )
        );
    }

    public PlayerSkin getSkin(BaseNPC npc_entity) {
        ModelData model_data = this.getModel(npc_entity);
        this.buildSkin(model_data);
        return this.skin_data.getOrDefault(model_data.texture(), DefaultPlayerSkin.get(UUID.randomUUID()));
    }

    public PlayerSkin getSkin(ModelData model_data) {
        this.buildSkin(model_data);
        return this.skin_data.getOrDefault(model_data.texture(), DefaultPlayerSkin.get(UUID.randomUUID()));
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
    public NPCData getNPCData(BaseNPC npc) {
        return this.getNPCMap().getOrDefault(npc.getID(), NPC_NOT_FOUND);
    }
}
