package mcsoc.npcmod.datatypes.cutscenes;

import java.util.Optional;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;


public enum CameraMode implements StringRepresentable {
    NORMAL {
        @Override
        public String toString() {
            return "normal";
        }

        @Override
        public boolean locked() {
            return false;
        }
    },
    UNLOCKED {
        @Override
        public String toString() {
            return "unlocked";
        }

        @Override
        public boolean locked() {
            return false;
        }
    },
    LOCKED {
        @Override
        public String toString() {
            return "locked";
        }
        
        @Override
        public boolean locked() {
            return true;
        }
    },
    PANNING {
        @Override
        public String toString() {
            return "pan_to";
        }

        @Override
        public boolean locked() {
            return true;
        }
    };

    public abstract boolean locked();

    @Override
    public String getSerializedName() {
        return this.toString();
    }

    public static Optional<CameraMode> fromString(String string) {
        for (CameraMode mode: CameraMode.values()) {
            if (mode.getSerializedName().equals(string)) return Optional.of(mode);
        }
        return Optional.empty();
    }

    public static final Codec<CameraMode> CODEC = StringRepresentable.fromValues(CameraMode::values);
    public static final StreamCodec<ByteBuf, CameraMode> PACKET_CODEC = ByteBufCodecs.fromCodec(CameraMode.CODEC);
}