package veveddo.npcmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import veveddo.npcmod.NpcMod;
import veveddo.npcmod.NpcModClient.ClientData;
import veveddo.npcmod.camera.ClientCameraControlData;
import veveddo.npcmod.camera.ClientCameraUtils;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.datatypes.cutscenes.CameraMode;
import veveddo.npcmod.entities.ClientEntityRegistration;
import veveddo.npcmod.entities.EntityRegistration;
import veveddo.npcmod.entities.camera.CameraClientEntity;
import veveddo.npcmod.entities.npc.NPCClientDataLoader;
import veveddo.npcmod.entities.npc.basicnpc.BaseNPCRendererPlayer;
import veveddo.npcmod.networking.packethandlers.PacketHandlers;


public class NpcModClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(NpcMod.MOD_ID);

	public static class ClientData implements ClientCameraControlData {
		private static ClientData INSTANCE = new ClientData();
		private ClientData() { /* delete */ }
		public static ClientData getInstance() {
			return INSTANCE;
		}

		private CameraMode camera_mode = CameraMode.NORMAL;
		
		private PositionData per_tick_pan_shift = null;
		private float ticks_left = 0;

		@Override
		public void setCameraModeRaw(CameraMode mode) {
			this.camera_mode = mode;
		}
		@Override
		public CameraMode getCameraMode() {
			return this.camera_mode;
		}

		public void panCamera(PositionData start_pos, PositionData target_pos, int ticks) {
			this.per_tick_pan_shift = target_pos.subtract(start_pos).divide(ticks);
			this.ticks_left = ticks;
			this.camera_mode = CameraMode.PANNING;
			this.setCameraPosition(start_pos);
		}

		public void tickMovement(float tick_delta) {
			if (this.camera_mode != CameraMode.PANNING || this.ticks_left == 0) return;

			if (Minecraft.getInstance().getCameraEntity() instanceof CameraClientEntity camera) {
				PositionData to_shift = this.per_tick_pan_shift.multiply(tick_delta);
				Vec3 shift_pos = camera.getEyePosition().add(to_shift.getPos());
				camera.absSnapTo(
					shift_pos.x, shift_pos.y, shift_pos.z, 
					camera.getYRot() + to_shift.yaw(), 
					camera.getXRot() + to_shift.pitch()
				);
			}
			
			this.ticks_left -= tick_delta;
			if (this.ticks_left <= 0) {
				this.setCameraMode(CameraMode.NORMAL);
			}
		}
	}


	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientEntityRegistration.registerEntities();
        EntityRenderers.register(EntityRegistration.BASIC_NPC, BaseNPCRendererPlayer::new);
		EntityRenderers.register(EntityRegistration.MOVING_NPC, BaseNPCRendererPlayer::new);

		ClientCameraUtils.registerDismountKeybind();

		NPCClientDataLoader.getInstance();
		PacketHandlers.registerHandlers();

		// WorldRenderEvents.START.register(ctx -> ClientData.getInstance().tickMovement(ctx.tickCounter().getLastFrameDuration()));
	}
}