package mcsoc.npcmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.commands.CommandRegistrationHandler;
import mcsoc.npcmod.cutscenes.CutsceneHandler;
import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.eventhandlers.NPCInteractEvent;
import mcsoc.npcmod.eventhandlers.PerServerTickEvent;
import mcsoc.npcmod.eventhandlers.PlayerJoinEvent;
import mcsoc.npcmod.networking.NetworkingIdentifiers;


public class NpcMod implements ModInitializer {
	public static final String MOD_ID = "npc-mod";
	public static final Random rand = new Random(1);

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		// register commands
		CommandRegistrationHandler.registerCommands();

		EntityRegistration.registerEntities();

		// event handlers
		NPCInteractEvent.registerEvent();
		PlayerJoinEvent.registerEvent();
		PerServerTickEvent.registerEvent();
		
		NetworkingIdentifiers.registerPackets();

		NpcModServerDataStorage.getInstance();
		CutsceneHandler.getInstance();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			CutsceneHandler.getInstance().setWorld(server.getOverworld());
		});
	}
}