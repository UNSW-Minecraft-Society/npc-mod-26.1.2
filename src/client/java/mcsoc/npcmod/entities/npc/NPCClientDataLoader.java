package mcsoc.npcmod.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.yggdrasil.ProfileResult;

import mcsoc.npcmod.dataloader.datastorage.npc.NPCDataStorage;
import mcsoc.npcmod.datatypes.npcs.DialogueData;
import mcsoc.npcmod.datatypes.npcs.ModelData;
import mcsoc.npcmod.datatypes.npcs.NPCData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;


public class NPCClientDataLoader implements NPCDataStorage {

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    private final Map<UUID, SkinTextures> skin_data = new HashMap<>();
    private static final NPCClientDataLoader INSTANCE = new NPCClientDataLoader();

    private NPCClientDataLoader() {}
    public static NPCClientDataLoader getInstance() {
        return INSTANCE;
    }

    private void fetchSkin(UUID uuid) {
        if (this.skin_data.containsKey(uuid)) return;
        this.skin_data.put(uuid, DefaultSkinHelper.getSkinTextures(UUID.randomUUID()));
        
        CompletableFuture.runAsync(() -> {
            ProfileResult result = MinecraftClient.getInstance().getSessionService().fetchProfile(uuid, true);
    
            MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(result.profile())
                    .thenAccept(skin -> this.skin_data.put(uuid, skin));
        });
    }

    public SkinTextures getSkin(UUID uuid) {
        this.fetchSkin(uuid);
        return this.skin_data.get(uuid);
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
