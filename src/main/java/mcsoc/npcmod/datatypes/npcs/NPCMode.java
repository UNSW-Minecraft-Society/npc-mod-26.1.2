package mcsoc.npcmod.datatypes.npcs;

import java.util.Optional;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;


public enum NPCMode implements StringRepresentable {
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
    public String getSerializedName() {
        return this.toString();
    }

    public static Optional<NPCMode> fromString(String string) {
        for (NPCMode mode: NPCMode.values()) {
            if (mode.getSerializedName().equals(string)) return Optional.of(mode);
        }
        return Optional.empty();
    }

    public static final Codec<NPCMode> CODEC = StringRepresentable.fromEnum(NPCMode::values);
    public static final StreamCodec<ByteBuf, NPCMode> PACKET_CODEC = ByteBufCodecs.fromCodec(NPCMode.CODEC);
}