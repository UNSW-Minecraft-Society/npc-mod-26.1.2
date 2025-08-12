package mcsoc.planetgame.Networking;

import mcsoc.planetgame.StateManagement.PlayerState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SyncPlayerDataS2CPayload(PlayerState state) implements CustomPayload {

    public static final CustomPayload.Id<SyncPlayerDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.PLAYER_SYNC_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SyncPlayerDataS2CPayload> CODEC = PacketCodec.tuple(
        PlayerState.PACKET_CODEC, SyncPlayerDataS2CPayload::state,
        SyncPlayerDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
