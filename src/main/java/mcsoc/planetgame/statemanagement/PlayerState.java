package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.math.Direction;

public record PlayerState(Direction grav_dir, GravityStrength grav_strength, PlayerFirstAbilities first_ability, PlayerSecondAbilities second_ability, PlayerThirdAbilities third_ability) {
    
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
