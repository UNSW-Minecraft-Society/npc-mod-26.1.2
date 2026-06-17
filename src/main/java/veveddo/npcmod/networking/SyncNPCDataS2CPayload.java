package veveddo.npcmod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import veveddo.npcmod.datatypes.npcs.NPCData;

public record SyncNPCDataS2CPayload(String id, NPCData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncNPCDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.NPC_DATA_REGISTER_ID);
    public static final PacketCodec<RegistryByteBuf, SyncNPCDataS2CPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SyncNPCDataS2CPayload::id,
        NPCData.PACKET_CODEC, SyncNPCDataS2CPayload::data,
        SyncNPCDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
