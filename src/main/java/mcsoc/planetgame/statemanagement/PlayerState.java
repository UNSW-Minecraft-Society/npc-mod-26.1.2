package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Direction;

public record PlayerState(Direction grav_dir, GravityStrength grav_strength, PlayerFirstAbilities first_ability) {
    public static enum PlayerFirstAbilities {
        NONE(0),
        FLIP(1),
        CONTROL(2);

        private final int identifier;

        private PlayerFirstAbilities(int id) {
            this.identifier = id;
        }

        public static final Codec<PlayerFirstAbilities> CODEC = Codec.INT.xmap(PlayerFirstAbilities::fromInt, PlayerFirstAbilities::asInt);

        public static final PacketCodec<ByteBuf, PlayerFirstAbilities> PACKET_CODEC = PacketCodecs.INTEGER.xmap(PlayerFirstAbilities::fromInt, PlayerFirstAbilities::asInt);

        public int asInt() {
            return this.identifier;
        }

        public static PlayerFirstAbilities fromInt(int i) {
            for (PlayerFirstAbilities a : PlayerFirstAbilities.values()) {
                if (a.asInt() == i) return a;
            }
            return PlayerFirstAbilities.NONE; 
        }
    }

    public static enum PlayerSecondAbilities {
        NONE,
        XRAY,
        DIGGER
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



    public Direction getCurrentPlayerGravityDirection() {
        return grav_dir;
    }

    public GravityStrength getPlayerGravStrengthModifier() {
        return grav_strength;
    }

    public PlayerFirstAbilities getPlayerFirstAbility() {
        return first_ability;
    }


    public PlayerState setPlayerGravStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(this.grav_dir, new_grav_strength, this.first_ability);
    }

    public PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(new_grav_dir, this.grav_strength, this.first_ability);
    }

    public PlayerState setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        return new PlayerState(this.grav_dir, this.grav_strength, new_first_ability);
    }

    public static PlayerState getDefaultPlayerState() {
        return new PlayerState(Direction.DOWN, GravityStrength.GRAV_STRENGTH_NORMAL, PlayerFirstAbilities.NONE);
    }
    
    public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Direction.CODEC.fieldOf("gravity_direction").forGetter(PlayerState::grav_dir),
        GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_strength),
        PlayerFirstAbilities.CODEC.fieldOf("player_first_ability").forGetter(PlayerState::first_ability)
    ).apply(inst, PlayerState::new));

    public static final PacketCodec<RegistryByteBuf, PlayerState> PACKET_CODEC = PacketCodec.tuple(
        Direction.PACKET_CODEC, PlayerState::grav_dir,
        GravityStrength.PACKET_CODEC, PlayerState::grav_strength,
        PlayerFirstAbilities.PACKET_CODEC, PlayerState::first_ability,
        PlayerState::new
    );
}
