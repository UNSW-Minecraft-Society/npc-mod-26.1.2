package mcsoc.planetgame.statemanagement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ManagedPlayerState {

    private record P1PlayerState(PlayerFirstAbilities first_ability, Direction gravity_dir, GravityStrength gravity_strength, boolean gravity_modified, boolean in_gravity_field) {
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
    
    private record P2PlayerState(PlayerSecondAbilities second_ability, boolean second_ability_active, double drill_charge, double drill_heat) {
        public static P2PlayerState withDefaultPlayerState() {
            return new P2PlayerState(PlayerSecondAbilities.getDefault(), false, 100, 0);
        }

        public P2PlayerState withPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
            return new P2PlayerState(new_second_ability, second_ability_active(), drill_charge(), drill_heat()); 
        }

        public P2PlayerState withPlayerSecondAbilityState(boolean ability_active) {
            return new P2PlayerState(second_ability(), ability_active, drill_charge(), drill_heat());
        }

        public P2PlayerState withPlayerDrillCharge(double new_drill_charge) {
            return new P2PlayerState(second_ability(), second_ability_active(), new_drill_charge, drill_heat());
        }

        public P2PlayerState withPlayerDrillHeat(double new_drill_heat) {
            return new P2PlayerState(second_ability(), second_ability_active(), drill_charge(), new_drill_heat);
        }

        public P2PlayerState withIncrementedPlayerDrillHeat(double added_drill_heat) {
            return new P2PlayerState(second_ability(), second_ability_active(), drill_charge(), drill_heat() + added_drill_heat);
        }

        public P2PlayerState withDecrementedPlayerDrillHeat(double removed_drill_heat) {
            return new P2PlayerState(second_ability(), second_ability_active(), drill_charge(), drill_heat() - removed_drill_heat);
        }

        public static Codec<P2PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(P2PlayerState::second_ability),
            Codec.BOOL.fieldOf("ability_active").forGetter(P2PlayerState::second_ability_active),
            Codec.DOUBLE.fieldOf("drill_charge").forGetter(P2PlayerState::drill_charge),
            Codec.DOUBLE.fieldOf("drill_heat").forGetter(P2PlayerState::drill_heat)
        ).apply(inst, P2PlayerState::new));
    }
    
    private record P3PlayerState(PlayerThirdAbilities third_ability, int third_ability_cooldown, boolean is_carrying, Vec3d dash_velocity, int dash_ticks_remaining) {
        public static P3PlayerState withDefaultPlayerState() {
            return new P3PlayerState(PlayerThirdAbilities.getDefault(), 0, false, Vec3d.ZERO, 0);
        }

        public P3PlayerState withPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
            return new P3PlayerState(new_third_ability, third_ability_cooldown(), is_carrying(), dash_velocity(), dash_ticks_remaining());
        }

        public P3PlayerState withPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
            return new P3PlayerState(third_ability(), new_third_ability_cooldown, is_carrying(), dash_velocity(), dash_ticks_remaining());
        }

        public P3PlayerState withDecrementedPlayerThirdAbilityCooldown() {
            return new P3PlayerState(third_ability(), third_ability_cooldown() - 1, is_carrying(), dash_velocity(), dash_ticks_remaining());
        }

        public P3PlayerState withIsPlayerCarrying(boolean carrying) {
            return new P3PlayerState(third_ability(), third_ability_cooldown(), carrying, dash_velocity(), dash_ticks_remaining());
        }

        public P3PlayerState withDashVelocity(Vec3d velocity) {
            return new P3PlayerState(third_ability(), third_ability_cooldown(), is_carrying(), velocity, dash_ticks_remaining());
        }

        public P3PlayerState withDashTicksRemaining(int ticks) {
            return new P3PlayerState(third_ability(), third_ability_cooldown(), is_carrying(), dash_velocity(), ticks);
        }

        public static Codec<P3PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(P3PlayerState::third_ability),
            Codec.INT.fieldOf("third_ability_cooldown").forGetter(P3PlayerState::third_ability_cooldown),
            Codec.BOOL.fieldOf("is_carrying").forGetter(P3PlayerState::is_carrying),
            Vec3d.CODEC.fieldOf("dash_velocity").forGetter(P3PlayerState::dash_velocity),
            Codec.INT.fieldOf("dash_ticks_remaining").forGetter(P3PlayerState::dash_ticks_remaining)
        ).apply(inst, P3PlayerState::new));
    }
    
    P1PlayerState p1_state;
    P2PlayerState p2_state;
    P3PlayerState p3_state;

    private P1PlayerState getP1State() {
        return p1_state;
    }

    private P2PlayerState getP2State() {
        return p2_state;
    }

    private P3PlayerState getP3State() {
        return p3_state;
    }

    ManagedPlayerState(P1PlayerState p1_state, P2PlayerState p2_state, P3PlayerState p3_state) {
        this.p1_state = p1_state;
        this.p2_state = p2_state;
        this.p3_state = p3_state;
    }

    ManagedPlayerState() {
        this(P1PlayerState.withDefaultPlayerState(), P2PlayerState.withDefaultPlayerState(), P3PlayerState.withDefaultPlayerState());
    }


    public static ManagedPlayerState getDefaultPlayerState() {
        return new ManagedPlayerState();
    }


    public PlayerFirstAbilities getPlayerFirstAbility() {
        return p1_state.first_ability();
    }

    public Direction getCurrentPlayerGravityDirection() {
        return p1_state.gravity_dir();
    }

    public GravityStrength getPlayerGravityStrengthModifier() {
        return p1_state.gravity_strength();
    }

    public boolean getPlayerGravityModified() {
        return p1_state.gravity_modified();
    }

    public boolean getIfPlayerInGravityField() {
        return p1_state.in_gravity_field();
    }


    public PlayerSecondAbilities getPlayerSecondAbility() {
        return p2_state.second_ability();
    }

    public boolean getPlayerSecondAbilityActive() {
        return p2_state.second_ability_active();
    }

    public double getPlayerDrillCharge() {
        return p2_state.drill_charge();
    }

    public double getPlayerDrillHeat() {
        return p2_state.drill_heat();
    }


    public PlayerThirdAbilities getPlayerThirdAbility() {
        return p3_state.third_ability();
    }

    public int getPlayerThirdAbilityCooldownTicks() {
        return p3_state.third_ability_cooldown();
    }

    public boolean getIfPlayerIsCarrying() {
        return p3_state.is_carrying();
    }

    public Vec3d getDashVelocity() {
        return p3_state.dash_velocity();
    }

    public int getDashTicksRemaining() {
        return p3_state.dash_ticks_remaining();
    }



    public void setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        this.p1_state = p1_state.withPlayerFirstAbility(new_first_ability);
    }

    public void setPlayerGravityDirection(Direction new_grav_dir) {
        this.p1_state = p1_state.withPlayerGravityDirection(new_grav_dir);
    }

    public void setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        this.p1_state = p1_state.withPlayerGravityStrengthModifier(new_grav_strength);
    }

    public void setPlayerGravityModified() {
        this.p1_state = p1_state.withPlayerGravityModified();
    }

    public void setPlayerInGravityField(boolean in_field) {
        this.p1_state = p1_state.withPlayerInGravityField(in_field);
    }


    public void setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        this.p2_state = p2_state.withPlayerSecondAbility(new_second_ability);
    }

    public void setPlayerSecondAbilityState(boolean xray_on) {
        this.p2_state = p2_state.withPlayerSecondAbilityState(xray_on);
    }

    public void setPlayerDrillCharge(double new_drill_charge) {
        this.p2_state = p2_state.withPlayerDrillCharge(new_drill_charge);
    }

    public void setPlayerDrillHeat(double new_drill_heat) {
        this.p2_state = p2_state.withPlayerDrillHeat(new_drill_heat);
    }

    public void incrementPlayerDrillHeat(double added_drill_heat) {
        this.p2_state = p2_state.withIncrementedPlayerDrillHeat(added_drill_heat);
    }

    public void decrementPlayerDrillHeat(double removed_drill_heat) {
        this.p2_state = p2_state.withDecrementedPlayerDrillHeat(removed_drill_heat);
    }


    public void setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        this.p3_state = p3_state.withPlayerThirdAbility(new_third_ability);
    }

    public void setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        this.p3_state = p3_state.withPlayerThirdAbilityCooldownTicks(new_third_ability_cooldown);
    }

    public void decrementPlayerThirdAbilityCooldown() {
        this.p3_state = p3_state.withDecrementedPlayerThirdAbilityCooldown();
    }

    public void setIfPlayerIsCarrying(boolean carrying) {
        this.p3_state = p3_state.withIsPlayerCarrying(carrying);
    }

    public void setDashVelocity(Vec3d velocity) {
        this.p3_state = p3_state.withDashVelocity(velocity);
    }

    public void setDashTicksRemaining(int ticks) {
        this.p3_state = p3_state.withDashTicksRemaining(ticks);
    }

    public void decrementDashTicksRemaining() {
        this.p3_state = p3_state.withDashTicksRemaining(p3_state.dash_ticks_remaining() - 1);
    }


    public void tick() {
        if (getDashTicksRemaining() > 0) {
            decrementDashTicksRemaining();
        }
        if (getPlayerThirdAbilityCooldownTicks() > 0) {
            decrementPlayerThirdAbilityCooldown();
        }
    }

    public static final Codec<ManagedPlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        P1PlayerState.CODEC.fieldOf("p1_state").forGetter(ManagedPlayerState::getP1State),
        P2PlayerState.CODEC.fieldOf("p2_state").forGetter(ManagedPlayerState::getP2State),
        P3PlayerState.CODEC.fieldOf("p3_state").forGetter(ManagedPlayerState::getP3State)
    ).apply(inst, ManagedPlayerState::new));
}
