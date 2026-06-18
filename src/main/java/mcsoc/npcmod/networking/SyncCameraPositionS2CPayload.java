package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.PositionData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncCameraPositionS2CPayload(PositionData data) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncCameraPositionS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.CAMERA_POSITION_SYNC_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCameraPositionS2CPayload> CODEC = StreamCodec.composite(
        PositionData.PACKET_CODEC, SyncCameraPositionS2CPayload::data,
        SyncCameraPositionS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}