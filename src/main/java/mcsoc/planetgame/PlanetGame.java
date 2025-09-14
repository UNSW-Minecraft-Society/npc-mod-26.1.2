package mcsoc.planetgame;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.planetgame.blocks.BlockRegistration;
import mcsoc.planetgame.eventhandlers.CommandRegistrationHandler;
import mcsoc.planetgame.eventhandlers.PerTickServerEvent;
import mcsoc.planetgame.eventhandlers.PlayerJoinServerEvent;
import mcsoc.planetgame.networking.packethandlers.GravityAbilityTriggerPacketEvent;


public class PlanetGame implements ModInitializer {
	public static final String MOD_ID = "planet-game";

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

		// event handlers
		PlayerJoinServerEvent.Register();
		CommandRegistrationHandler.Register();
		PerTickServerEvent.Register();

		// blocks, items, potions ect
		BlockRegistration.Register();
		
		// network packet handlers
		GravityAbilityTriggerPacketEvent.Register();
	}
}