package mcsoc.planetgame.statemanagement.enums;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum GravityStrength implements DoubleIdentifiable {

        GRAV_STRENGTH_NONE(DoubleIdentifiable.DEFAULT_ID),
        GRAV_STRENGTH_LOW(0.1D),
        GRAV_STRENGTH_NORMAL(1D),
        GRAV_STRENGTH_HIGH(3D);

        private Double val;

        private GravityStrength(Double d) {
            val = d;
        }

        public double getIdentifier() {
            return val;
        }

        public double getDouble() {
            return getIdentifier();
        }

        public static GravityStrength getDefault() {
            return GRAV_STRENGTH_NORMAL;
        }

        public static GravityStrength fromDouble(Double d) {
            return DoubleIdentifiable.fromIdentifier(d, GravityStrength.class);
        }

        public static final Codec<GravityStrength> CODEC = DoubleIdentifiable.getCodec(GravityStrength.class);
        public static final PacketCodec<ByteBuf, GravityStrength> PACKET_CODEC = DoubleIdentifiable.getPacketCodec(GravityStrength.class);
    }
