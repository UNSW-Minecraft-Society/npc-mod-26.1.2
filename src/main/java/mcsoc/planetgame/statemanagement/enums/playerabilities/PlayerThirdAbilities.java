package mcsoc.planetgame.statemanagement.enums.playerabilities;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

import mcsoc.planetgame.networking.enumcodecinterfaces.IntIdentifiable;

public enum PlayerThirdAbilities implements IntIdentifiable {
    NONE(IntIdentifiable.DEFAULT_ID),
    DASH_ADDITIVE(1),
    DASH_SET(3),
    THROW(2);

    private final int identifier;

    private PlayerThirdAbilities(int id) {
        this.identifier = id;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    public static PlayerThirdAbilities getDefault() {
        return NONE;
    }

    public static final Codec<PlayerThirdAbilities> CODEC = IntIdentifiable.getCodec(PlayerThirdAbilities.class);
    public static final PacketCodec<ByteBuf, PlayerThirdAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerThirdAbilities.class);
}