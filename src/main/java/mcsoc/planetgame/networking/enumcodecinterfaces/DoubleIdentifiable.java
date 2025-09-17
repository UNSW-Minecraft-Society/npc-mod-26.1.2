package mcsoc.planetgame.networking.enumcodecinterfaces;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;


public interface DoubleIdentifiable {

    public double getIdentifier();
    public static final double DEFAULT_ID = 0D;

    public static <T extends Enum<T> & DoubleIdentifiable> T getDefault(Class<T> t_class) {
        return fromIdentifier(DEFAULT_ID, t_class);
    }

    public static <T extends Enum<T> & DoubleIdentifiable> Double getIdentifier(T t) {
        return t.getIdentifier();
    }

    public static <T extends Enum<T> & DoubleIdentifiable> T fromIdentifier(Double i, Class<T> t_class) {
        for (T t : t_class.getEnumConstants()) {
            if (t.getIdentifier() == i) return t;
        }
        if (i == DEFAULT_ID) {
            throw new UnsupportedOperationException("Enum with default ID not defined!");
        }
        return getDefault(t_class);
    }

    public static <T extends Enum<T> & DoubleIdentifiable> Codec<T> getCodec(Class<T> t_class) {
        return Codec.DOUBLE.xmap(d -> fromIdentifier(d, t_class), T::getIdentifier);
    }

    public static <T extends Enum<T> & DoubleIdentifiable> PacketCodec<ByteBuf, T> getPacketCodec(Class<T> t_class) {
        return PacketCodecs.DOUBLE.xmap(d -> fromIdentifier(d, t_class), T::getIdentifier);
    }
}