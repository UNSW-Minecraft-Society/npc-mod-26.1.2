package mcsoc.npcmod.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.yggdrasil.ProfileResult;

import mcsoc.npcmod.dataloader.NPCDataStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;


public class NPCClientDataLoader extends NPCDataStorage {   

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
}
