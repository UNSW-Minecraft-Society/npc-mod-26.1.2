package mcsoc.npcmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.datatypes.cutscenes.PositionData;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.basicnpc.BaseNPCRendererPlayer;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.packethandlers.PacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;


public class NpcModClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(NpcMod.MOD_ID);

	public static class ClientData {
		private static ClientData INSTANCE = new ClientData();
		private ClientData() { /* delete */ }
		public static ClientData getInstance() {
			return INSTANCE;
		}

		private CameraMode camera_mode = CameraMode.NORMAL;
		private PositionData camera_position = null;

		public void setCameraMode(CameraMode mode) {
			this.camera_mode = mode;
		}
		public CameraMode getCameraMode() {
			return this.camera_mode;
		}
		public void setCameraPosition(PositionData pos) {
			this.camera_position = pos;
		}
		public PositionData getCameraPosition() {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			Vec3d camera_pos = player.getClientCameraPosVec(0);
			if (this.getCameraMode().equals(CameraMode.NORMAL)) {
				return new PositionData(camera_pos.x, camera_pos.y, camera_pos.z, player.getPitch(), player.getYaw());
			}
			return this.camera_position.addPos(camera_pos);
		}
	}


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

        EntityRendererRegistry.register(EntityRegistration.BASIC_NPC, BaseNPCRendererPlayer::new);
		EntityRendererRegistry.register(EntityRegistration.MOVING_NPC, BaseNPCRendererPlayer::new);

		NPCClientDataLoader.getInstance();
		PacketHandlers.registerHandlers();
	}
}