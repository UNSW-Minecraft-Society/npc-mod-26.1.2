package veveddo.npcmod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import veveddo.npcmod.datatypes.PositionData;

public record SyncCameraPositionS2CPayload(PositionData data) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncCameraPositionS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.CAMERA_POSITION_SYNC_ID);
    public static final PacketCodec<RegistryByteBuf, SyncCameraPositionS2CPayload> CODEC = PacketCodec.tuple(
        PositionData.PACKET_CODEC, SyncCameraPositionS2CPayload::data,
        SyncCameraPositionS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}