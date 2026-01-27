package mcsoc.npcmod.networking;

import mcsoc.npcmod.datatypes.PositionData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;


public record CameraPanS2CPayload(PositionData from, PositionData to, int ticks) implements CustomPayload {
    
    public static final CustomPayload.Id<CameraPanS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.CAMERA_PAN_TRIGGER_ID);
    public static final PacketCodec<RegistryByteBuf, CameraPanS2CPayload> CODEC = PacketCodec.tuple(
        PositionData.PACKET_CODEC, CameraPanS2CPayload::from,
        PositionData.PACKET_CODEC, CameraPanS2CPayload::to,
        PacketCodecs.INTEGER, CameraPanS2CPayload::ticks,
        CameraPanS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}