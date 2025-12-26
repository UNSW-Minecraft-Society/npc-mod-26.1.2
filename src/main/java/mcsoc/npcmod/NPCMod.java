package mcsoc.npcmod;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.commands.CommandRegistrationHandler;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.NPCServerDataLoader;
import mcsoc.npcmod.eventhandlers.NPCInteractEvent;


public class NPCMod implements ModInitializer {
	public static final String MOD_ID = "npc-mod";

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

		// event handlers
		NPCInteractEvent.registerEvent();
		
		EntityRegistration.registerEntities();

		NPCServerDataLoader.getInstance();
	}
}