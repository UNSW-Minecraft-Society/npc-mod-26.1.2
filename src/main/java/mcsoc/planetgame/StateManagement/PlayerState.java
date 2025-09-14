package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.Direction;

public record PlayerState(Direction grav_dir, Double grav_mod) {

        public PlayerState setCurrentPlayerGravityDirection(Direction new_grav_dir) {
            return new PlayerState(new_grav_dir, this.grav_mod);
        }

        public Direction getCurrentPlayerGravityDirection() {
            return grav_dir;
        }

        public PlayerState setPlayerGravStrengthModifier(Double grav_strength_mod) {
            return new PlayerState(this.grav_dir, grav_strength_mod);
        }

        public Double getPlayerGravStrengthModifier() {
            return grav_mod;
        }


        public static PlayerState getDefaultPlayerState() {
            return new PlayerState(Direction.DOWN, Double.valueOf(1));
        }
        
        public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Direction.CODEC.fieldOf("gravity_direction").forGetter(PlayerState::grav_dir),
            Codec.DOUBLE.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_mod)
        ).apply(inst, PlayerState::new));

        public static final PacketCodec<RegistryByteBuf, PlayerState> PACKET_CODEC = PacketCodec.tuple(
            Direction.PACKET_CODEC, PlayerState::grav_dir,
            PacketCodecs.DOUBLE, PlayerState::grav_mod,
            PlayerState::new
        );
    }
