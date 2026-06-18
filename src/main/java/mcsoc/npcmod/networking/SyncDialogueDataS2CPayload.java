package mcsoc.npcmod.networking;


import mcsoc.npcmod.datatypes.npcs.DialogueData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncDialogueDataS2CPayload(String id, DialogueData data) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncDialogueDataS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.DIALOGUE_DATA_REGISTER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDialogueDataS2CPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncDialogueDataS2CPayload::id,
        DialogueData.PACKET_CODEC, SyncDialogueDataS2CPayload::data,
        SyncDialogueDataS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
