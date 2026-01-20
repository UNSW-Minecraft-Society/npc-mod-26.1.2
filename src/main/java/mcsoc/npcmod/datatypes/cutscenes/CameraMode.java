package mcsoc.npcmod.datatypes.cutscenes;

import java.util.Optional;

import com.mojang.serialization.Codec;

import io.netty.buffer.ByteBuf;
import mcsoc.npcmod.datatypes.npcs.NPCMode;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.StringIdentifiable;

public enum CameraMode implements StringIdentifiable {
    NORMAL {
        @Override
        public String toString() {
            return "normal";
        }
    },
    UNLOCKED {
        @Override
        public String toString() {
            return "unlocked";
        }
    },
    LOCKED {
        @Override
        public String toString() {
            return "locked";
        }
    };

    @Override
    public abstract String toString();

    @Override
    public String asString() {
        return this.toString();
    }

    public static Optional<CameraMode> fromString(String string) {
        for (CameraMode mode: CameraMode.values()) {
            if (mode.asString().equals(string)) return Optional.of(mode);
        }
        return Optional.empty();
    }

    public static final Codec<CameraMode> CODEC = StringIdentifiable.createCodec(CameraMode::values);
    public static final PacketCodec<ByteBuf, CameraMode> PACKET_CODEC = PacketCodecs.codec(CameraMode.CODEC);
}