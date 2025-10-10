package mcsoc.planetgame.statemanagement.playerstate;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Direction;

public record PlayerState(P1PlayerState p1_state, P2PlayerState p2_state, P3PlayerState p3_state) {
    
    private static record P1PlayerState(PlayerFirstAbilities first_ability, Direction gravity_dir, GravityStrength gravity_strength, boolean gravity_modified, boolean in_gravity_field) {
        public static P1PlayerState withDefaultPlayerState() {
            return new P1PlayerState(PlayerFirstAbilities.getDefault(), Direction.DOWN, GravityStrength.getDefault(), false, false);
        }

        public P1PlayerState withPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
            return new P1PlayerState(new_first_ability, gravity_dir(), gravity_strength(), gravity_modified(), in_gravity_field());
        }

        public P1PlayerState withPlayerGravityDirection(Direction new_grav_dir) {
            return new P1PlayerState(first_ability(), new_grav_dir, gravity_strength(), gravity_modified(), in_gravity_field());
        }

        public P1PlayerState withPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
            return new P1PlayerState(first_ability(), gravity_dir(), new_grav_strength, gravity_modified(), in_gravity_field());
        }

        public P1PlayerState withPlayerGravityModified() {
            return new P1PlayerState(first_ability(), gravity_dir(), gravity_strength(), true, in_gravity_field());
        }

        public P1PlayerState withPlayerInGravityField(boolean in_field) {
            return new P1PlayerState(first_ability(), gravity_dir(), gravity_strength(), in_gravity_field(), in_field);
        }

        public static Codec<P1PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerFirstAbilities.CODEC.fieldOf("player_first_ability").forGetter(P1PlayerState::first_ability),
            Direction.CODEC.fieldOf("gravity_direction").forGetter(P1PlayerState::gravity_dir),
            GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(P1PlayerState::gravity_strength),
            Codec.BOOL.fieldOf("gravity_modified").forGetter(P1PlayerState::gravity_modified),
            Codec.BOOL.fieldOf("in_gravity_field").forGetter(P1PlayerState::in_gravity_field)
        ).apply(inst, P1PlayerState::new));
    }
    
    private static record P2PlayerState(PlayerSecondAbilities second_ability, boolean second_ability_active, double drill_charge, double drill_heat) {
        public static P2PlayerState withDefaultPlayerState() {
            return new P2PlayerState(PlayerSecondAbilities.getDefault(), false);
        }

        public P2PlayerState withPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
            return second_ability;
        }

        public P2PlayerState withPlayerSecondAbilityState(boolean xray_on) {
        }

        public boolean getPlayerXrayState() {
            return xray_on;
        }

        public P2PlayerState setPlayerXrayState(boolean xray_on) {
            return new P2PlayerState(getPlayerSecondAbility(), xray_on);
        }

        public static Codec<P2PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(P2PlayerState::second_ability),
            Codec.BOOL.fieldOf("xray_on").forGetter(P2PlayerState::second_ability_active),
        ).apply(inst, P2PlayerState::new));
    }
    
    private static record P3PlayerState(PlayerThirdAbilities third_ability, int third_ability_cooldown, boolean is_carrying) {
        public static P3PlayerState withDefaultPlayerState() {
            return new P3PlayerState(PlayerThirdAbilities.getDefault(), 0, false);
        }

        public P3PlayerState withPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
            return new P3PlayerState(new_third_ability, third_ability_cooldown(), is_carrying());
        }

        public P3PlayerState withPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
            return new P3PlayerState(third_ability(), new_third_ability_cooldown, is_carrying());
        }

        public P3PlayerState withDecrementedPlayerThirdAbilityCooldown() {
            return new P3PlayerState(third_ability(), third_ability_cooldown() - 1, is_carrying());
        }

        public P3PlayerState withIsPlayerCarrying(boolean carrying) {
            return new P3PlayerState(third_ability(), third_ability_cooldown(), carrying);
        }

        public static Codec<P3PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(P3PlayerState::third_ability),
            Codec.INT.fieldOf("third_ability_cooldown").forGetter(P3PlayerState::third_ability_cooldown),
            Codec.BOOL.fieldOf("is_carrying").forGetter(P3PlayerState::is_carrying)
        ).apply(inst, P3PlayerState::new));
    }
    

    protected PlayerFirstAbilities getPlayerFirstAbility() {
        return p1_state.first_ability();
    }

    protected Direction getCurrentPlayerGravityDirection() {
        return p1_state.gravity_dir();
    }

    protected GravityStrength getPlayerGravityStrengthModifier() {
        return p1_state.gravity_strength();
    }

    protected boolean getPlayerGravityModified() {
        return p1_state.gravity_modified();
    }

    protected boolean getIfPlayerInGravityField() {
        return p1_state.in_gravity_field();
    }

    protected PlayerSecondAbilities getPlayerSecondAbility() {
        return p2_state.second_ability();
    }

    protected boolean getPlayerXrayState() {
        return p2_state.getPlayerXrayState();
    }

    protected PlayerThirdAbilities getPlayerThirdAbility() {
        return p3_state.third_ability();
    }

    protected int getPlayerThirdAbilityCooldownTicks() {
        return p3_state.third_ability_cooldown();
    }

    protected boolean getIfPlayerIsCarrying() {
        return p3_state.is_carrying();
    }


    protected static PlayerState withDefaultPlayerState() {
        return new PlayerState(P1PlayerState.withDefaultPlayerState(), P2PlayerState.withDefaultPlayerState(), P3PlayerState.withDefaultPlayerState());
    }

    protected PlayerState withPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        return new PlayerState(p1_state.withPlayerFirstAbility(new_first_ability), p2_state, p3_state);
    }

    protected PlayerState withPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(p1_state.withPlayerGravityDirection(new_grav_dir), p2_state, p3_state);
    }

    protected PlayerState withPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(p1_state.withPlayerGravityStrengthModifier(new_grav_strength), p2_state, p3_state);
    }

    protected PlayerState withPlayerGravityModified() {
        return new PlayerState(p1_state.withPlayerGravityModified(), p2_state, p3_state);
    }

    protected PlayerState withPlayerInGravityField(boolean in_field) {
        return new PlayerState(p1_state.withPlayerInGravityField(in_field), p2_state, p3_state);
    }

    protected PlayerState withPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        return new PlayerState(p1_state, p2_state.withPlayerSecondAbility(new_second_ability), p3_state);
    }

    protected PlayerState setPlayerXrayState(boolean xray_on) {
        return new PlayerState(p1_state, p2_state.setPlayerXrayState(xray_on), p3_state);
    }


    protected PlayerState withPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        return new PlayerState(p1_state, p2_state, p3_state.withPlayerThirdAbility(new_third_ability));
    }

    protected PlayerState withPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        return new PlayerState(p1_state, p2_state, p3_state.withPlayerThirdAbilityCooldownTicks(new_third_ability_cooldown));
    }

    protected PlayerState withDecrementedPlayerThirdAbilityCooldown() {
        return new PlayerState(p1_state, p2_state, p3_state.withDecrementedPlayerThirdAbilityCooldown());
    }

    protected PlayerState withIsPlayerCarrying(boolean carrying) {
        return new PlayerState(p1_state, p2_state, p3_state.withIsPlayerCarrying(carrying));
    }


    protected PlayerState tick() {
        if (getPlayerThirdAbilityCooldownTicks() > 0) {
            return this.withDecrementedPlayerThirdAbilityCooldown();
        } 
        return this;
    }


    protected static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        P1PlayerState.CODEC.fieldOf("p1_state").forGetter(PlayerState::p1_state),
        P2PlayerState.CODEC.fieldOf("p2_state").forGetter(PlayerState::p2_state),
        P3PlayerState.CODEC.fieldOf("p3_state").forGetter(PlayerState::p3_state)
    ).apply(inst, PlayerState::new));
}
