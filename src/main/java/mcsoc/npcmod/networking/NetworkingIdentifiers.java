package mcsoc.npcmod.networking;

import mcsoc.npcmod.NPCMod;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

public abstract class NetworkingIdentifiers {
    public static final Identifier MODEL_DATA_REGISTER = Identifier.of(NPCMod.MOD_ID, "sync_register_model_data_s2c_packet");
    public static final Identifier DIALOGUE_DATA_REGISTER = Identifier.of(NPCMod.MOD_ID, "sync_register_dialogue_data_s2c_packet");
    public static final Identifier NPC_DATA_REGISTER = Identifier.of(NPCMod.MOD_ID, "sync_register_npc_data_s2c_packet");

    public static void registerPackets() {
        PayloadTypeRegistry.playS2C().register(SyncModelDataS2CPayload.ID, SyncModelDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncDialogueDataS2CPayload.ID, SyncDialogueDataS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncNPCDataS2CPayload.ID, SyncNPCDataS2CPayload.CODEC);
    }
}
