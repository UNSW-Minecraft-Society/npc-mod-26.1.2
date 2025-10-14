package mcsoc.planetgame;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.planetgame.networking.NetworkingIdentifiers;
import mcsoc.planetgame.networking.packethandlers.FirstAbilityTriggerPacketEvent;
import mcsoc.planetgame.networking.packethandlers.SecondAbilityTriggerPacketEvent;
import mcsoc.planetgame.networking.packethandlers.ThirdAbilityTriggerPacketEvent;
import mcsoc.planetgame.blocks.BlockEntityRegistration;
import mcsoc.planetgame.blocks.BlockRegistration;
import mcsoc.planetgame.entities.EntityRegistration;
import mcsoc.planetgame.entities.damagesources.DamageSourceRegistration;
import mcsoc.planetgame.eventhandlers.PerTickServerEvent;
import mcsoc.planetgame.eventhandlers.PlayerJoinServerEvent;


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

		// network packet handlers
		NetworkingIdentifiers.registerPackets();
		FirstAbilityTriggerPacketEvent.registerHandler();
		SecondAbilityTriggerPacketEvent.registerHandler();
		ThirdAbilityTriggerPacketEvent.registerHandler();

		// register commands
		CommandRegistrationHandler.registerCommands();

		// event handlers
		PlayerJoinServerEvent.registerEvent();
		PerTickServerEvent.registerEvent();

		// blocks, items, potions ect
		BlockRegistration.registerBlocks();
		BlockEntityRegistration.registerBlockEntities();
		
		EntityRegistration.registerEntities();

		DamageSourceRegistration.registerDamageSources();
	}
}