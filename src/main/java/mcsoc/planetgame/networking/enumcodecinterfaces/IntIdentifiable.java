package mcsoc.planetgame.networking.enumcodecinterfaces;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;


public interface IntIdentifiable {

    public int getIdentifier();
    public static final int DEFAULT_ID = 0;

    public static <T extends Enum<T> & IntIdentifiable> T getDefault(Class<T> t_class) {
        return fromIdentifier(DEFAULT_ID, t_class);
    }

    public static <T extends Enum<T> & IntIdentifiable> Integer getIdentifier(T t) {
        return t.getIdentifier();
    }

    public static <T extends Enum<T> & IntIdentifiable> T fromIdentifier(Integer i, Class<T> t_class) {
        for (T t : t_class.getEnumConstants()) {
            if (t.getIdentifier() == i) return t;
        }
        if (i == DEFAULT_ID) {
            throw new UnsupportedOperationException("Enum with default ID not defined!");
        }
        return getDefault(t_class);
    }

    public static <T extends Enum<T> & IntIdentifiable> Codec<T> getCodec(Class<T> t_class) {
        return Codec.INT.xmap(i -> fromIdentifier(i, t_class), T::getIdentifier);
    }

    public static <T extends Enum<T> & IntIdentifiable> PacketCodec<ByteBuf, T> getPacketCodec(Class<T> t_class) {
        return PacketCodecs.INTEGER.xmap(i -> fromIdentifier(i, t_class), T::getIdentifier);
    }
}