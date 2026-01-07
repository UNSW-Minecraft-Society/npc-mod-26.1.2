package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class PlayerJoinEvent {
    public static void registerEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			NpcModServerDataStorage.getInstance().syncClientData(handler.getPlayer());
		});
    }
}
