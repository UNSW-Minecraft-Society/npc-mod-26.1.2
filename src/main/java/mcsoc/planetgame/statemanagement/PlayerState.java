package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mcsoc.planetgame.GameEffects;
import mcsoc.planetgame.networking.enumcodecinterfaces.*;

import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Direction;

public record PlayerState(
    PlayerFirstAbilities first_ability, Direction grav_dir, GravityStrength grav_strength, Boolean gravity_modified,
    PlayerSecondAbilities second_ability, 
    PlayerThirdAbilities third_ability, int third_ability_cooldown) {
    
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
            return NONE;
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
            return NONE;
        }

        public static final Codec<PlayerSecondAbilities> CODEC = IntIdentifiable.getCodec(PlayerSecondAbilities.class);
        public static final PacketCodec<ByteBuf, PlayerSecondAbilities> PACKET_CODEC = IntIdentifiable.getPacketCodec(PlayerSecondAbilities.class);
    }

    public static enum PlayerThirdAbilities implements IntIdentifiable {
        NONE(IntIdentifiable.DEFAULT_ID),
        DASH_ADDITIVE(1),
        DASH_SET(3),
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
            return NONE;
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
            return GRAV_STRENGTH_NORMAL;
        }

        public static GravityStrength fromDouble(Double d) {
            return DoubleIdentifiable.fromIdentifier(d, GravityStrength.class);
        }

        public static final Codec<GravityStrength> CODEC = DoubleIdentifiable.getCodec(GravityStrength.class);
        public static final PacketCodec<ByteBuf, GravityStrength> PACKET_CODEC = DoubleIdentifiable.getPacketCodec(GravityStrength.class);
    };


    public PlayerFirstAbilities getPlayerFirstAbility() {
        return first_ability;
    }

    public Direction getCurrentPlayerGravityDirection() {
        return grav_dir;
    }

    public GravityStrength getPlayerGravityStrengthModifier() {
        return grav_strength;
    }

    public Boolean getPlayerGravityModified() {
        return gravity_modified;
    }

    public PlayerSecondAbilities getPlayerSecondAbility() {
        return second_ability;
    }

    public PlayerThirdAbilities getPlayerThirdAbility() {
        return third_ability;
    }

    public int getPlayerThirdAbilityCooldownTicks() {
        return third_ability_cooldown;
    }


    public PlayerState setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        return new PlayerState(new_first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.second_ability, this.third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(this.first_ability, new_grav_dir, this.grav_strength, this.gravity_modified, this.second_ability, this.third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(this.first_ability, this.grav_dir, new_grav_strength, this.gravity_modified, this.second_ability, this.third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerGravityModified() {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, Boolean.TRUE, this.second_ability, this.third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, new_second_ability, this.third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.second_ability, new_third_ability, this.third_ability_cooldown);
    }

    public PlayerState setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.second_ability, this.third_ability, new_third_ability_cooldown);
    }

    public PlayerState decrementPlayerThirdAbilityCooldown() {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.second_ability, this.third_ability, this.third_ability_cooldown - 1);
    }

    public static PlayerState getDefaultPlayerState() {
        return new PlayerState(PlayerFirstAbilities.getDefault(), Direction.DOWN, GravityStrength.getDefault(), Boolean.FALSE, PlayerSecondAbilities.getDefault(), PlayerThirdAbilities.getDefault(), 0);
    }

        return new PlayerState(Direction.DOWN, GravityStrength.getDefault(), PlayerFirstAbilities.getDefault(), PlayerSecondAbilities.getDefault(), PlayerThirdAbilities.getDefault());
    }


    public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        PlayerFirstAbilities.CODEC.fieldOf("player_first_ability").forGetter(PlayerState::first_ability),
        Direction.CODEC.fieldOf("gravity_direction").forGetter(PlayerState::grav_dir),
        GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_strength),
        Codec.BOOL.fieldOf("gravity_modified").forGetter(PlayerState::gravity_modified),
        PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(PlayerState::second_ability),
        PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(PlayerState::third_ability),
        Codec.INT.fieldOf("third_ability_cooldown").forGetter(PlayerState::third_ability_cooldown)
    ).apply(inst, PlayerState::new));

    // public static final PacketCodec<ByteBuf, PlayerState> PACKET_CODEC = PacketCodecs.codec(CODEC);
}
