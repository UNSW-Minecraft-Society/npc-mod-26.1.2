package veveddo.npcmod.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.util.Identifier;
import veveddo.npcmod.NpcMod;
import veveddo.npcmod.dataloader.datastorage.npc.NPCDataStorage;
import veveddo.npcmod.datatypes.npcs.DialogueData;
import veveddo.npcmod.datatypes.npcs.ModelData;
import veveddo.npcmod.datatypes.npcs.NPCData;
import veveddo.npcmod.entities.npc.BaseNPC;


public class NPCClientDataLoader implements NPCDataStorage {

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    private final Map<Identifier, SkinTextures> skin_data = new HashMap<>();
    private static final NPCClientDataLoader INSTANCE = new NPCClientDataLoader();

    private NPCClientDataLoader() {}
    public static NPCClientDataLoader getInstance() {
        return INSTANCE;
    }

    public void buildSkin(ModelData model_data) {
        if (this.skin_data.containsKey(model_data.texture())) return;

        Identifier texture_id = model_data.texture();

        try {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getTextureManager().getOrDefault(texture_id, null) == null) {
                AbstractTexture texture = new ResourceTexture(texture_id);
                client.getTextureManager().registerTexture(texture_id, texture);
            }
        } catch (Exception e) {
            NpcMod.LOGGER.error("Failed to bind custom NPC texture: " + texture_id + " -> ", e);
        }

        this.skin_data.put(texture_id, 
            new SkinTextures(
                model_data.texture(), 
                null, 
                null, 
                null, 
                model_data.is_slim() ? Model.SLIM : Model.WIDE, 
                true
            )
        );
    }

    public SkinTextures getSkin(ModelData model_data) {
        this.buildSkin(model_data);
        return this.skin_data.getOrDefault(model_data.texture(), DefaultSkinHelper.getSkinTextures(UUID.randomUUID()));
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
