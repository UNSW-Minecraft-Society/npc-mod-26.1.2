package mcsoc.planetgame.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SyncPlayerDrillingDataS2CPayload(boolean xray_active) implements CustomPayload {

    public static final CustomPayload.Id<SyncPlayerDrillingDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.PLAYER_SYNC_DRILLERS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SyncPlayerDrillingDataS2CPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.BOOL, SyncPlayerDrillingDataS2CPayload::xray_active,
        SyncPlayerDrillingDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}