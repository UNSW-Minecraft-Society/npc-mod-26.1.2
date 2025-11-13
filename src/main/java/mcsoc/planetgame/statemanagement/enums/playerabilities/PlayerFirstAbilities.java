package mcsoc.planetgame.statemanagement.enums.playerabilities;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

import mcsoc.planetgame.networking.enumcodecinterfaces.IntIdentifiable;

public enum PlayerFirstAbilities implements IntIdentifiable {
    NONE(IntIdentifiable.DEFAULT_ID),
    FLIP(1),
    CONTROL(2);

    private final int identifier;

    private PlayerFirstAbilities(int id) {
        this.identifier = id;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    public static PlayerFirstAbilities getDefault() {
        return NONE;
    }

    public static final Codec<PlayerFirstAbilities> CODEC = IntIdentifiable.getCodec(PlayerFirstAbilities.class);
    public static final PacketCodec<ByteBuf, PlayerFirstAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerFirstAbilities.class);
}