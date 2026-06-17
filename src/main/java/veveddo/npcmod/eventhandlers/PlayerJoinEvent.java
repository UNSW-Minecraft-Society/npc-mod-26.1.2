package veveddo.npcmod.eventhandlers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;

public class PlayerJoinEvent {
    public static void registerEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			NpcModServerDataStorage.getInstance().syncClientData(handler.getPlayer());
		});
    }
}
