package mcsoc.npcmod.networking;

import mcsoc.npcmod.NpcMod;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.ResourceLocation;

public abstract class NetworkingIdentifiers {
    public static final ResourceLocation MODEL_DATA_REGISTER_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_register_model_data_s2c_packet");
    public static final ResourceLocation DIALOGUE_DATA_REGISTER_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_register_dialogue_data_s2c_packet");
    public static final ResourceLocation NPC_DATA_REGISTER_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_register_npc_data_s2c_packet");
    public static final ResourceLocation MOVING_NPC_DATA_REGISTER_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_register_moving_npc_data_s2c_packet");

    public static final ResourceLocation CAMERA_POSITION_SYNC_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_camera_position_s2c_packet");
    public static final ResourceLocation CAMERA_MODE_SYNC_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "sync_camera_mode_s2c_packet");
    public static final ResourceLocation CAMERA_PAN_TRIGGER_ID = ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, "trigger_camera_pan_s2c_packet");

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(SyncModelDataS2CPayload.ID, SyncModelDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDialogueDataS2CPayload.ID, SyncDialogueDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncNPCDataS2CPayload.ID, SyncNPCDataS2CPayload.CODEC);
        
        PayloadTypeRegistry.playS2C().register(SyncMovingNPCDataS2CPayload.ID, SyncMovingNPCDataS2CPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncCameraPositionS2CPayload.ID, SyncCameraPositionS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncCameraModeS2CPayload.ID, SyncCameraModeS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CameraPanS2CPayload.ID, CameraPanS2CPayload.CODEC);
    }
}
