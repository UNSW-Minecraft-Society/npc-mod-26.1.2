package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.npcs.NPCData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncNPCDataS2CPayload(String id, NPCData data) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncNPCDataS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.NPC_DATA_REGISTER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncNPCDataS2CPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncNPCDataS2CPayload::id,
        NPCData.PACKET_CODEC, SyncNPCDataS2CPayload::data,
        SyncNPCDataS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
