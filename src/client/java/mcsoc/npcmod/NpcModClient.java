package mcsoc.npcmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.camera.ClientCameraUtils;
import mcsoc.npcmod.camera.ClientCameraControlData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.ClientEntityRegistration;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.basicnpc.BaseNPCRendererPlayer;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.packethandlers.PacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;


public class NpcModClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(NpcMod.MOD_ID);

	public static class ClientData implements ClientCameraControlData {
		private static ClientData INSTANCE = new ClientData();
		private ClientData() { /* delete */ }
		public static ClientData getInstance() {
			return INSTANCE;
		}

		private CameraMode camera_mode = CameraMode.NORMAL;

		@Override
		public void setCameraModeRaw(CameraMode mode) {
			this.camera_mode = mode;
		}
		@Override
		public CameraMode getCameraMode() {
			return this.camera_mode;
		}
	}


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientEntityRegistration.registerEntities();
        EntityRendererRegistry.register(EntityRegistration.BASIC_NPC, BaseNPCRendererPlayer::new);
		EntityRendererRegistry.register(EntityRegistration.MOVING_NPC, BaseNPCRendererPlayer::new);

		ClientCameraUtils.registerDismountKeybind();

		NPCClientDataLoader.getInstance();
		PacketHandlers.registerHandlers();
	}
}