package mcsoc.planetgame.statemanagement.enums.playerabilities;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import mcsoc.planetgame.statemanagement.enums.IntIdentifiable;
import net.minecraft.network.codec.PacketCodec;

public enum PlayerSecondAbilities implements IntIdentifiable {
    NONE(IntIdentifiable.DEFAULT_ID),
    XRAY(1),
    DRILLING(2);

    private final int identifier;

    private PlayerSecondAbilities(int id) {
        this.identifier = id;
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    public static PlayerSecondAbilities getDefault() {
        return NONE;
    }

    public static final Codec<PlayerSecondAbilities> CODEC = IntIdentifiable.getCodec(PlayerSecondAbilities.class);
    public static final PacketCodec<ByteBuf, PlayerSecondAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerSecondAbilities.class);
}
