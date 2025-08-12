package mcsoc.planetgame.StateManagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record PlayerState(Boolean flipped, Double grav_mod) {

        public PlayerState setIsPlayerFlipped(Boolean flipped_state) {
            return new PlayerState(flipped_state, this.grav_mod);
        }

        public Boolean getIsPlayerFlipped() {
            return flipped;
        }

        public PlayerState setPlayerGravStrengthModifier(Double grav_strength_mod) {
            return new PlayerState(this.flipped, grav_strength_mod);
        }

        public Double getPlayerGravStrengthModifier() {
            return grav_mod;
        }


        public static PlayerState getDefaultPlayerState() {
            return new PlayerState(Boolean.FALSE, Double.valueOf(1));
        }
        
        public static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.fieldOf("is_flipped").forGetter(PlayerState::flipped),
            Codec.DOUBLE.fieldOf("gravity_strength_modifier").forGetter(PlayerState::grav_mod)
        ).apply(inst, PlayerState::new));

        public static final PacketCodec<RegistryByteBuf, PlayerState> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, PlayerState::flipped,
            PacketCodecs.DOUBLE, PlayerState::grav_mod,
            PlayerState::new
        );
    }
