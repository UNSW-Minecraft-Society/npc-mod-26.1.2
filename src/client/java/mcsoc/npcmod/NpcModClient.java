package mcsoc.npcmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.camera.ClientCameraUtils;
import mcsoc.npcmod.camera.ClientCameraControlData;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.entities.ClientEntityRegistration;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import mcsoc.npcmod.entities.npc.basicnpc.BaseNPCRendererPlayer;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.networking.packethandlers.PacketHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;


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

			if (MinecraftClient.getInstance().getCameraEntity() instanceof CameraClientEntity camera) {
				PositionData to_shift = this.per_tick_pan_shift.multiply(tick_delta);
				camera.refreshPositionAndAngles(
					camera.getPos().add(to_shift.getPos()), 
					camera.getYaw() + to_shift.yaw(), 
					camera.getPitch() + to_shift.pitch()
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
        EntityRendererRegistry.register(EntityRegistration.BASIC_NPC, BaseNPCRendererPlayer::new);
		EntityRendererRegistry.register(EntityRegistration.MOVING_NPC, BaseNPCRendererPlayer::new);

		ClientCameraUtils.registerDismountKeybind();

		NPCClientDataLoader.getInstance();
		PacketHandlers.registerHandlers();

		WorldRenderEvents.START.register(ctx -> ClientData.getInstance().tickMovement(ctx.tickCounter().getLastFrameDuration()));
	}
}