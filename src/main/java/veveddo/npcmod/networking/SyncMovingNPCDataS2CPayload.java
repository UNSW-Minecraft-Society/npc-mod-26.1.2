package veveddo.npcmod.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import veveddo.npcmod.datatypes.npcs.NPCData;

public record SyncMovingNPCDataS2CPayload(String id, NPCData data) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncMovingNPCDataS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.MOVING_NPC_DATA_REGISTER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncMovingNPCDataS2CPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncMovingNPCDataS2CPayload::id,
        NPCData.PACKET_CODEC, SyncMovingNPCDataS2CPayload::data,
        SyncMovingNPCDataS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
