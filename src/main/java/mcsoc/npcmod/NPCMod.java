package mcsoc.npcmod;

import net.fabricmc.api.ModInitializer;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.commands.CommandRegistrationHandler;
import mcsoc.npcmod.dataloader.NPCJsonDataParser;
import mcsoc.npcmod.dataloader.NPCServerDataLoader;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.eventhandlers.NPCInteractEvent;
import mcsoc.npcmod.eventhandlers.PlayerJoinEvent;
import mcsoc.npcmod.networking.NetworkingIdentifiers;


public class NPCMod implements ModInitializer {
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

		// event handlers
		PlayerJoinEvent.registerHandler();
		
		NetworkingIdentifiers.registerPackets();

		NPCJsonDataParser.getInstance();
		NPCServerDataLoader.getInstance();
	}
}