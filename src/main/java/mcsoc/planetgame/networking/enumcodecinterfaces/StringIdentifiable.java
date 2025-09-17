package mcsoc.planetgame.networking.enumcodecinterfaces;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;


public interface StringIdentifiable {

    public String getIdentifier();
    public static final String DEFAULT_ID = "";
    
    public static <T extends Enum<T> & StringIdentifiable> T getDefault(Class<T> t_class) {
        return fromIdentifier(DEFAULT_ID, t_class);
    }

    public static <T extends Enum<T> & StringIdentifiable> String getIdentifier(T t) {
        return t.getIdentifier();
    }

    public static <T extends Enum<T> & StringIdentifiable> T fromIdentifier(String i, Class<T> t_class) {
        for (T t : t_class.getEnumConstants()) {
            if (t.getIdentifier().equals(i)) return t;
        }
        if (i.equals(DEFAULT_ID)) {
            throw new UnsupportedOperationException("Enum with default ID not defined!");
        }
        return getDefault(t_class);
    }

    public static <T extends Enum<T> & StringIdentifiable> Codec<T> getCodec(Class<T> t_class) {
        return Codec.STRING.xmap(i -> fromIdentifier(i, t_class), T::getIdentifier);
    }

    public static <T extends Enum<T> & StringIdentifiable> PacketCodec<ByteBuf, T> getPacketCodec(Class<T> t_class) {
        return PacketCodecs.STRING.xmap(i -> fromIdentifier(i, t_class), T::getIdentifier);
    }
}