package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.dataloader.NPCServerDataLoader;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class PlayerJoinEvent {
    public static void registerHandler() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			NPCServerDataLoader.getInstance().syncClientData(handler.getPlayer());
		});
    }
}
