package mcsoc.planetgame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.planetgame.entities.throwablerock.ThrowableRockModel;
import mcsoc.planetgame.entities.throwablerock.ThrowableRockRenderer;
import mcsoc.planetgame.keybinds.GravityKeybind;
import mcsoc.planetgame.keybinds.MobilityKeybind;
import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import mcsoc.planetgame.networking.packethandlers.GravitySyncPacketEvent;
import mcsoc.planetgame.registration.entities.EntityRegistration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class PlanetGameClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(PlanetGame.MOD_ID);

	private static ClientPlayerState player_state = ClientPlayerState.getDefaultPlayerState();

	public static ClientPlayerState getPlayerState() {
		return PlanetGameClient.player_state;
	}

	public static void setPlayerState(ClientPlayerState player_state) {
		PlanetGameClient.player_state = player_state;
	}

	public static void updateGravityState(SyncPlayerGravityDataS2CPayload payload) {
		setPlayerState(getPlayerState().updateGravityFromPayload(payload));
	}


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		GravitySyncPacketEvent.register();
		
		GravityKeybind.register();
		MobilityKeybind.register();

		EntityModelLayerRegistry.registerModelLayer(ThrowableRockModel.ROCK, ThrowableRockModel::getTexturedModelData);
        EntityRendererRegistry.register(EntityRegistration.ROCK, ThrowableRockRenderer::new);
	}
}