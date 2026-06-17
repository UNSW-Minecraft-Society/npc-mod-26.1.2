package veveddo.npcmod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import veveddo.npcmod.datatypes.cutscenes.CameraMode;


public record SyncCameraModeS2CPayload(CameraMode mode) implements CustomPayload {
    
    public static final CustomPayload.Id<SyncCameraModeS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.CAMERA_MODE_SYNC_ID);
    public static final PacketCodec<RegistryByteBuf, SyncCameraModeS2CPayload> CODEC = PacketCodec.tuple(
        CameraMode.PACKET_CODEC, SyncCameraModeS2CPayload::mode,
        SyncCameraModeS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}