package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record SyncCameraModeS2CPayload(CameraMode mode) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncCameraModeS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.CAMERA_MODE_SYNC_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCameraModeS2CPayload> CODEC = StreamCodec.composite(
        CameraMode.PACKET_CODEC, SyncCameraModeS2CPayload::mode,
        SyncCameraModeS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}