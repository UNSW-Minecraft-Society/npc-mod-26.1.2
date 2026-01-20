package mcsoc.npcmod.datatypes.npcs;

import java.util.Optional;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

public enum NPCMode implements StringIdentifiable {
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

    public static Optional<NPCMode> fromString(String string) {
        for (NPCMode mode: NPCMode.values()) {
            if (mode.asString().equals(string)) return Optional.of(mode);
        }
        return Optional.empty();
    }

    public static final Codec<NPCMode> CODEC = StringIdentifiable.createCodec(NPCMode::values);
    public static final PacketCodec<ByteBuf, NPCMode> PACKET_CODEC = PacketCodecs.codec(NPCMode.CODEC);
}