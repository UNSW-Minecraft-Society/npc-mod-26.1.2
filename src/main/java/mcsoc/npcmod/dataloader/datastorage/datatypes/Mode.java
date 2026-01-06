package mcsoc.npcmod.dataloader.datastorage.datatypes;

import java.util.Optional;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

public enum Mode implements StringIdentifiable {
    BACKGROUND {
        @Override
        public String toString() {
            return "background";
        }
    },
    MAIN {
        @Override
        public String toString() {
            return "main";
        }
    },
    MOVING {
        @Override
        public String toString() {
            return "moving";
        }
    };

    @Override
    public abstract String toString();

    @Override
    public String asString() {
        return this.toString();
    }

    public static Optional<Mode> fromString(String string) {
        for (Mode mode: Mode.values()) {
            if (mode.toString().equals(string)) return Optional.of(mode);
        }
        return Optional.empty();
    }

    public static final Codec<Mode> CODEC = StringIdentifiable.createCodec(Mode::values);
    public static final PacketCodec<ByteBuf, Mode> PACKET_CODEC = PacketCodecs.codec(Mode.CODEC);
}