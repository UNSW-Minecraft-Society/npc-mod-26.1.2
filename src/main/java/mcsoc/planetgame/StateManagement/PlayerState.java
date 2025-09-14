package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Direction;

public record PlayerState(Direction grav_dir, GravityStrength grav_mod) {
    public static enum PlayerAbilities1 {
        NONE,
        FLIP,
        CONTROL
    }

    public static enum GravityStrength {
        GRAV_STRENGTH_NONE(0D),
        GRAV_STRENGTH_LOW(0.1D),
        GRAV_STRENGTH_NORMAL(1D),
        GRAV_STRENGTH_HIGH(3D);

        private Double val;

        private GravityStrength(Double d) {
            val = d;
        }

        public Double getDouble() {
            return val;
        }

        public static GravityStrength fromDouble(Double d) {
            for (GravityStrength s : GravityStrength.values()) {
                if (s.getDouble() == d) {
                    return s;
                }
            }
            return GRAV_STRENGTH_NONE;
        }

        public static Double getValue(GravityStrength s) {
            return s.getDouble();
        }

        public static final Codec<GravityStrength> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.DOUBLE.fieldOf("value").forGetter(GravityStrength::getDouble)
        ).apply(inst, GravityStrength::fromDouble));

        public static final PacketCodec<RegistryByteBuf, GravityStrength> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.DOUBLE, s -> getValue(s),
            GravityStrength::fromDouble
        );
    };



    public PlayerState setCurrentPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(new_grav_dir, this.grav_mod);
    }

    public Direction getCurrentPlayerGravityDirection() {
        return grav_dir;
    }

    public PlayerState setPlayerGravStrengthModifier(GravityStrength grav_strength_mod) {
        return new PlayerState(this.grav_dir, grav_strength_mod);
    }

    public GravityStrength getPlayerGravStrengthModifier() {
        return grav_mod;
    }


    public static PlayerState getDefaultPlayerState() {
        return new PlayerState(Direction.DOWN, GravityStrength.GRAV_STRENGTH_NORMAL);
    }
    
    public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Direction.CODEC.fieldOf("gravity_direction").forGetter(PlayerState::grav_dir),
        GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_mod)
    ).apply(inst, PlayerState::new));

    public static final PacketCodec<RegistryByteBuf, PlayerState> PACKET_CODEC = PacketCodec.tuple(
        Direction.PACKET_CODEC, PlayerState::grav_dir,
        GravityStrength.PACKET_CODEC, PlayerState::grav_mod,
        PlayerState::new
    );
}
