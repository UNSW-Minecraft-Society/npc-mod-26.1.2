package mcsoc.planetgame.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.yggdrasil.ProfileResult;

import mcsoc.planetgame.PlanetGameClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;

public class NPCClientDataLoader {   

    private final Map<UUID, SkinTextures> skin_data = new HashMap<>();
    private static final NPCClientDataLoader INSTANCE = new NPCClientDataLoader();

    private NPCClientDataLoader() {}

    private void fetchSkin(UUID uuid) {
        if (this.skin_data.containsKey(uuid)) return;
        PlanetGameClient.LOGGER.info("fetching: {}", uuid);
        this.skin_data.put(uuid, DefaultSkinHelper.getSkinTextures(UUID.randomUUID()));
        
        CompletableFuture.runAsync(() -> {
            ProfileResult result = MinecraftClient.getInstance().getSessionService().fetchProfile(uuid, true);
    
            MinecraftClient.getInstance().getSkinProvider().fetchSkinTextures(result.profile())
                    .thenAccept(skin -> {
                        this.skin_data.put(uuid, skin);
                    });
        });
    }

    public static NPCClientDataLoader getInstance() {
        return INSTANCE;
    }

    public SkinTextures getSkin(UUID uuid) {
        fetchSkin(uuid);
        return this.skin_data.get(uuid);
    }
}
