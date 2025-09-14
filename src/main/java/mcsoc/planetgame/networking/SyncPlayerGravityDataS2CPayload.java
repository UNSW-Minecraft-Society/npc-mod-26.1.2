package mcsoc.planetgame.networking;

import mcsoc.planetgame.statemanagement.PlayerState.GravityStrength;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Direction;

public record SyncPlayerGravityDataS2CPayload(Direction gravity_direction, GravityStrength gravity_strength_modifier) implements CustomPayload {

    public static final CustomPayload.Id<SyncPlayerGravityDataS2CPayload> ID = new CustomPayload.Id<>(NetworkingIdentifiers.PLAYER_SYNC_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, SyncPlayerGravityDataS2CPayload> CODEC = PacketCodec.tuple(
        Direction.PACKET_CODEC, SyncPlayerGravityDataS2CPayload::gravity_direction,
        GravityStrength.PACKET_CODEC, SyncPlayerGravityDataS2CPayload::gravity_strength_modifier,
        SyncPlayerGravityDataS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
