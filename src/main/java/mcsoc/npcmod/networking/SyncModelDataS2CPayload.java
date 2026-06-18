package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.npcs.ModelData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record SyncModelDataS2CPayload(String id, ModelData data) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncModelDataS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.MODEL_DATA_REGISTER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncModelDataS2CPayload> CODEC = StreamCodec.composite(
        ByteBufCodecs.STRING_UTF8, SyncModelDataS2CPayload::id,
        ModelData.PACKET_CODEC, SyncModelDataS2CPayload::data,
        SyncModelDataS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
