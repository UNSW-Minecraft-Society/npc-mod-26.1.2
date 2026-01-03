package mcsoc.npcmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.NPCClientDataLoader;
import mcsoc.npcmod.entities.npc.basicnpc.BasicNPCRendererPlayer;
import mcsoc.npcmod.networking.packethandlers.SyncDialogueDataPacketEvent;
import mcsoc.npcmod.networking.packethandlers.SyncModelDataPacketEvent;
import mcsoc.npcmod.networking.packethandlers.SyncNPCDataPacketEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;


public class NPCModClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger(NPCMod.MOD_ID);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

        EntityRendererRegistry.register(EntityRegistration.BASIC_NPC, BasicNPCRendererPlayer::new);
		EntityRendererRegistry.register(EntityRegistration.MOVING_NPC, BasicNPCRendererPlayer::new);

		NPCClientDataLoader.getInstance();
		SyncDialogueDataPacketEvent.registerHandler();
		SyncModelDataPacketEvent.registerHandler();
		SyncNPCDataPacketEvent.registerHandler();
	}
}