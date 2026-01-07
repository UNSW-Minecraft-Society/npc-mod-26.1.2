package mcsoc.npcmod.eventhandlers;

import mcsoc.npcmod.cutscenes.CutsceneHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class PerServerTickEvent {

    public static void registerEvent() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            CutsceneHandler.getInstance().tickReader();
        });
    }
}
