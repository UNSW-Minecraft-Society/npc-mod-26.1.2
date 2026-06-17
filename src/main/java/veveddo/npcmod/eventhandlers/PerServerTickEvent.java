package veveddo.npcmod.eventhandlers;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import veveddo.npcmod.cutscenes.CutsceneHandler;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;

public class PerServerTickEvent {

    public static void registerEvent() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            CutsceneHandler.getInstance().tickReader();
            NpcModServerDataStorage.getInstance().tick();
        });
    }
}
