package mcsoc.npcmod.networking;

import mcsoc.npcmod.dataloader.NPCDataStorage.NPCData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SyncNPCDataS2CPayload(String id, NPCData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncNPCDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.NPC_DATA_REGISTER);
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
