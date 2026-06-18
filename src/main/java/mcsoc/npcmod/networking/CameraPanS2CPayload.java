package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.PositionData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record CameraPanS2CPayload(PositionData from, PositionData to, int ticks) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<CameraPanS2CPayload> ID = new CustomPacketPayload.Type<>(NetworkingIdentifiers.CAMERA_PAN_TRIGGER_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, CameraPanS2CPayload> CODEC = StreamCodec.composite(
        PositionData.PACKET_CODEC, CameraPanS2CPayload::from,
        PositionData.PACKET_CODEC, CameraPanS2CPayload::to,
        ByteBufCodecs.INT, CameraPanS2CPayload::ticks,
        CameraPanS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}