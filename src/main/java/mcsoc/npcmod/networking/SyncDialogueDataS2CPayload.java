package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.npcs.DialogueData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SyncDialogueDataS2CPayload(String id, DialogueData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncDialogueDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.DIALOGUE_DATA_REGISTER);
    public static final PacketCodec<RegistryByteBuf, SyncDialogueDataS2CPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SyncDialogueDataS2CPayload::id,
        DialogueData.PACKET_CODEC, SyncDialogueDataS2CPayload::data,
        SyncDialogueDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
