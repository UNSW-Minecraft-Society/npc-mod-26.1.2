package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import net.minecraft.util.math.Direction;

public record PlayerState(
    PlayerFirstAbilities first_ability, Direction grav_dir, GravityStrength grav_strength, boolean gravity_modified, boolean in_gravity_field,
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

    public boolean getPlayerGravityModified() {
        return gravity_modified;
    }

    public boolean getPlayerIsInGravityField() {
        return in_gravity_field;
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
        return new PlayerState(new_first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(this.first_ability, new_grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(this.first_ability, this.grav_dir, new_grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerGravityModified() {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, Boolean.TRUE, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerInGravityField(boolean in_field) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, in_field, this.second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, new_second_ability, this.third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, new_third_ability, this.third_ability_cooldown, this.is_carrying);
    }

    public PlayerState setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, new_third_ability_cooldown, this.is_carrying);
    }

    public PlayerState decrementPlayerThirdAbilityCooldown() {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown - 1, this.is_carrying);
    }

    public PlayerState setPlayerIsCarrying(boolean carrying) {
        return new PlayerState(this.first_ability, this.grav_dir, this.grav_strength, this.gravity_modified, this.in_gravity_field, this.second_ability, this.third_ability, this.third_ability_cooldown, carrying);
    }

    public static PlayerState getDefaultPlayerState() {
        return new PlayerState(PlayerFirstAbilities.getDefault(), Direction.DOWN, GravityStrength.getDefault(), false, false, PlayerSecondAbilities.getDefault(), PlayerThirdAbilities.getDefault(), 0, false);
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
        Codec.BOOL.fieldOf("in_gravity_field").forGetter(PlayerState::in_gravity_field),
        PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(PlayerState::second_ability),
        PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(PlayerState::third_ability),
        Codec.INT.fieldOf("third_ability_cooldown").forGetter(PlayerState::third_ability_cooldown),
        Codec.BOOL.fieldOf("is_carrying").forGetter(PlayerState::is_carrying)
    ).apply(inst, PlayerState::new));

    // public static final PacketCodec<ByteBuf, PlayerState> PACKET_CODEC = PacketCodecs.codec(CODEC);
}
