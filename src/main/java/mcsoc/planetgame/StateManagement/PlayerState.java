package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import net.minecraft.util.math.Direction;

public record PlayerState(
    PlayerFirstAbilities first_ability, Direction grav_dir, GravityStrength grav_strength, Boolean gravity_modified,
    PlayerSecondAbilities second_ability, 
    PlayerThirdAbilities third_ability, int third_ability_cooldown, boolean is_carrying) {
    

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

    public PlayerState tick() {
        if (this.third_ability_cooldown > 0) {
            return decrementPlayerThirdAbilityCooldown();
        } return this;
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
