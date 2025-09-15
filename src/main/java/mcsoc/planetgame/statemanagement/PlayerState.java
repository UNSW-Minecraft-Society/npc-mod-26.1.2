package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import mcsoc.planetgame.statemanagement.enumcodecinterfaces.DoubleIdentifiable;
import mcsoc.planetgame.statemanagement.enumcodecinterfaces.IntIdentifiable;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Direction;

public record PlayerState(Direction grav_dir, GravityStrength grav_strength, PlayerFirstAbilities first_ability, PlayerSecondAbilities second_ability, PlayerThirdAbilities third_ability) {
    
    public static enum PlayerFirstAbilities implements IntIdentifiable {
        NONE(IntIdentifiable.DEFAULT_ID),
        FLIP(1),
        CONTROL(2);

        private final int identifier;

        private PlayerFirstAbilities(int id) {
            this.identifier = id;
        }

        @Override
        public int getIdentifier() {
            return identifier;
        }

        public static PlayerFirstAbilities getDefault() {
            return IntIdentifiable.getDefault(PlayerFirstAbilities.class);
        }

        public static final Codec<PlayerFirstAbilities> CODEC = IntIdentifiable.getCodec(PlayerFirstAbilities.class);
        public static final PacketCodec<ByteBuf, PlayerFirstAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerFirstAbilities.class);
    }

    public static enum PlayerSecondAbilities implements IntIdentifiable {
        NONE(IntIdentifiable.DEFAULT_ID),
        XRAY(1),
        DRILLING(2);

        private final int identifier;

        private PlayerSecondAbilities(int id) {
            this.identifier = id;
        }

        @Override
        public int getIdentifier() {
            return identifier;
        }

        public static PlayerSecondAbilities getDefault() {
            return IntIdentifiable.getDefault(PlayerSecondAbilities.class);
        }

        public static final Codec<PlayerSecondAbilities> CODEC = IntIdentifiable.getCodec(PlayerSecondAbilities.class);
        public static final PacketCodec<ByteBuf, PlayerSecondAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerSecondAbilities.class);
    }

    public static enum PlayerThirdAbilities implements IntIdentifiable {
        NONE(IntIdentifiable.DEFAULT_ID),
        DASH(1),
        THROW(2);

        private final int identifier;

        private PlayerThirdAbilities(int id) {
            this.identifier = id;
        }

        @Override
        public int getIdentifier() {
            return identifier;
        }

        public static PlayerThirdAbilities getDefault() {
            return IntIdentifiable.getDefault(PlayerThirdAbilities.class);
        }

        public static final Codec<PlayerThirdAbilities> CODEC = IntIdentifiable.getCodec(PlayerThirdAbilities.class);
        public static final PacketCodec<ByteBuf, PlayerThirdAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerThirdAbilities.class);
    }


    public enum GravityStrength implements DoubleIdentifiable {

        GRAV_STRENGTH_NONE(DoubleIdentifiable.DEFAULT_ID),
        GRAV_STRENGTH_LOW(0.1D),
        GRAV_STRENGTH_NORMAL(1D),
        GRAV_STRENGTH_HIGH(3D);

        private Double val;

        private GravityStrength(Double d) {
            val = d;
        }

        public double getIdentifier() {
            return val;
        }

        public double getDouble() {
            return getIdentifier();
        }

        public static GravityStrength getDefault() {
            return DoubleIdentifiable.getDefault(GravityStrength.class);
        }

        public static GravityStrength fromDouble(Double d) {
            return DoubleIdentifiable.fromIdentifier(d, GravityStrength.class);
        }

        public static final Codec<GravityStrength> CODEC = DoubleIdentifiable.getCodec(GravityStrength.class);
        public static final PacketCodec<ByteBuf, GravityStrength> PACKET_CODEC = DoubleIdentifiable.getPacketCodec(GravityStrength.class);
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

    public PlayerSecondAbilities getPlayerSecondAbility() {
        return second_ability;
    }

    public PlayerThirdAbilities getPlayerThirdAbility() {
        return third_ability;
    }


    public PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(new_grav_dir, this.grav_strength, this.first_ability, this.second_ability, this.third_ability);
    }

    public PlayerState setPlayerGravStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(this.grav_dir, new_grav_strength, this.first_ability, this.second_ability, this.third_ability);
    }

    public PlayerState setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        return new PlayerState(this.grav_dir, this.grav_strength, new_first_ability, this.second_ability, this.third_ability);
    }

    public PlayerState setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        return new PlayerState(this.grav_dir, this.grav_strength, this.first_ability, new_second_ability, this.third_ability);
    }

    public PlayerState setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        return new PlayerState(this.grav_dir, this.grav_strength, this.first_ability, this.second_ability, new_third_ability);
    }

    public static PlayerState getDefaultPlayerState() {
        return new PlayerState(Direction.DOWN, GravityStrength.getDefault(), PlayerFirstAbilities.getDefault(), PlayerSecondAbilities.getDefault(), PlayerThirdAbilities.getDefault());
    }
    
    public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        Direction.CODEC.fieldOf("gravity_direction").forGetter(PlayerState::grav_dir),
        GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_strength),
        PlayerFirstAbilities.CODEC.fieldOf("player_first_ability").forGetter(PlayerState::first_ability),
        PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(PlayerState::second_ability),
        PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(PlayerState::third_ability)
    ).apply(inst, PlayerState::new));

    public static final PacketCodec<RegistryByteBuf, PlayerState> PACKET_CODEC = PacketCodec.tuple(
        Direction.PACKET_CODEC, PlayerState::grav_dir,
        GravityStrength.PACKET_CODEC, PlayerState::grav_strength,
        PlayerFirstAbilities.PACKET_CODEC, PlayerState::first_ability,
        PlayerSecondAbilities.PACKET_CODEC, PlayerState::second_ability,
        PlayerThirdAbilities.PACKET_CODEC, PlayerState::third_ability,
        PlayerState::new
    );
}
