package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.npcs.ModelData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;


public record SyncModelDataS2CPayload(String id, ModelData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncModelDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.MODEL_DATA_REGISTER_ID);
    public static final PacketCodec<RegistryByteBuf, SyncModelDataS2CPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SyncModelDataS2CPayload::id,
        ModelData.PACKET_CODEC, SyncModelDataS2CPayload::data,
        SyncModelDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
