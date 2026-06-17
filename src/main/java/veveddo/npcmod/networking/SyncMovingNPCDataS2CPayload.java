package veveddo.npcmod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import veveddo.npcmod.datatypes.npcs.NPCData;

public record SyncMovingNPCDataS2CPayload(String id, NPCData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncMovingNPCDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.MOVING_NPC_DATA_REGISTER_ID);
    public static final PacketCodec<RegistryByteBuf, SyncMovingNPCDataS2CPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SyncMovingNPCDataS2CPayload::id,
        NPCData.PACKET_CODEC, SyncMovingNPCDataS2CPayload::data,
        SyncMovingNPCDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
