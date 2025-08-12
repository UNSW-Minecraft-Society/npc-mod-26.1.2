package mcsoc.planetgame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.planetgame.EventHandlers.ReceivePacketEvent;
import mcsoc.planetgame.StateManagement.PlayerState;
import net.fabricmc.api.ClientModInitializer;

public class PlanetGameClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(PlanetGame.MOD_ID);

	private static PlayerState player_state = PlayerState.getDefaultPlayerState();

	public static PlayerState getPlayerState() {
		return PlanetGameClient.player_state;
	}

	public static void setPlayerState(PlayerState player_state) {
		PlanetGameClient.player_state = player_state;
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ReceivePacketEvent.Register();
	}
}