package mcsoc.planetgame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class PlanetGameClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(PlanetGame.MOD_ID);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}