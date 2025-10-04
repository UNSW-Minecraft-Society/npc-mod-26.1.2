package mcsoc.planetgame.statemanagement.playerstate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;

import net.minecraft.util.math.Direction;

public record PlayerState(P1PlayerState p1_state, P2PlayerState p2_state, P3PlayerState p3_state) {
    
    private static record P1PlayerState(PlayerFirstAbilities first_ability, Direction grav_dir, GravityStrength grav_strength, boolean gravity_modified, boolean in_gravity_field) {
        public static P1PlayerState getDefaultPlayerState() {
            return new P1PlayerState(PlayerFirstAbilities.getDefault(), Direction.DOWN, GravityStrength.getDefault(), false, false);
        }
        
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

        public boolean getIfPlayerInGravityField() {
            return in_gravity_field;
        }

        public P1PlayerState setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
            return new P1PlayerState(new_first_ability, getCurrentPlayerGravityDirection(), getPlayerGravityStrengthModifier(), getIfPlayerInGravityField(), getPlayerGravityModified());
        }

        public P1PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
            return new P1PlayerState(getPlayerFirstAbility(), new_grav_dir, getPlayerGravityStrengthModifier(), getIfPlayerInGravityField(), getPlayerGravityModified());
        }

        public P1PlayerState setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
            return new P1PlayerState(getPlayerFirstAbility(), getCurrentPlayerGravityDirection(), new_grav_strength, getIfPlayerInGravityField(), getPlayerGravityModified());
        }

        public P1PlayerState setPlayerGravityModified() {
            return new P1PlayerState(getPlayerFirstAbility(), getCurrentPlayerGravityDirection(), getPlayerGravityStrengthModifier(), true, getIfPlayerInGravityField());
        }

        public P1PlayerState setPlayerInGravityField(boolean in_field) {
            return new P1PlayerState(getPlayerFirstAbility(), getCurrentPlayerGravityDirection(), getPlayerGravityStrengthModifier(), getPlayerGravityModified(), in_field);
        }


        public static Codec<P1PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerFirstAbilities.CODEC.fieldOf("player_first_ability").forGetter(P1PlayerState::getPlayerFirstAbility),
            Direction.CODEC.fieldOf("gravity_direction").forGetter(P1PlayerState::getCurrentPlayerGravityDirection),
            GravityStrength.CODEC.fieldOf("gravity_strength_modifier").forGetter(P1PlayerState::getPlayerGravityStrengthModifier),
            Codec.BOOL.fieldOf("gravity_modified").forGetter(P1PlayerState::getPlayerGravityModified),
            Codec.BOOL.fieldOf("in_gravity_field").forGetter(P1PlayerState::getIfPlayerInGravityField)
        ).apply(inst, P1PlayerState::new));
    }
    
    private static record P2PlayerState(PlayerSecondAbilities second_ability, boolean xray_on) {
        public static P2PlayerState getDefaultPlayerState() {
            return new P2PlayerState(PlayerSecondAbilities.getDefault(), false);
        }

        public PlayerSecondAbilities getPlayerSecondAbility() {
            return second_ability;
        }

        public P2PlayerState setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
            return new P2PlayerState(new_second_ability, false);
        }

        public boolean getPlayerXrayState() {
            return xray_on;
        }

        public P2PlayerState setPlayerXrayState(boolean xray_on) {
            return new P2PlayerState(getPlayerSecondAbility(), xray_on);
        }

        public static Codec<P2PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerSecondAbilities.CODEC.fieldOf("player_second_ability").forGetter(P2PlayerState::getPlayerSecondAbility),
            Codec.BOOL.fieldOf("xray_on").forGetter(P2PlayerState::xray_on)
        ).apply(inst, P2PlayerState::new));
    }
    private static record P3PlayerState(PlayerThirdAbilities third_ability, int third_ability_cooldown, boolean is_carrying) {
        public static P3PlayerState getDefaultPlayerState() {
            return new P3PlayerState(PlayerThirdAbilities.getDefault(), 0, false);
        }
        
        public PlayerThirdAbilities getPlayerThirdAbility() {
            return third_ability;
        }

        public int getPlayerThirdAbilityCooldownTicks() {
            return third_ability_cooldown;
        }

        public boolean getIfPlayerIsCarrying() {
            return is_carrying;
        }

        public P3PlayerState setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
            return new P3PlayerState(new_third_ability, getPlayerThirdAbilityCooldownTicks(), getIfPlayerIsCarrying());
        }

        public P3PlayerState setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
            return new P3PlayerState(getPlayerThirdAbility(), new_third_ability_cooldown, getIfPlayerIsCarrying());
        }

        public P3PlayerState decrementPlayerThirdAbilityCooldown() {
            return new P3PlayerState(getPlayerThirdAbility(), getPlayerThirdAbilityCooldownTicks() - 1, getIfPlayerIsCarrying());
        }

        public P3PlayerState setPlayerIsCarrying(boolean carrying) {
            return new P3PlayerState(getPlayerThirdAbility(), getPlayerThirdAbilityCooldownTicks(), carrying);
        }

        public static Codec<P3PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            PlayerThirdAbilities.CODEC.fieldOf("player_third_ability").forGetter(P3PlayerState::getPlayerThirdAbility),
            Codec.INT.fieldOf("third_ability_cooldown").forGetter(P3PlayerState::getPlayerThirdAbilityCooldownTicks),
            Codec.BOOL.fieldOf("is_carrying").forGetter(P3PlayerState::getIfPlayerIsCarrying)
        ).apply(inst, P3PlayerState::new));
    }
    

    protected PlayerFirstAbilities getPlayerFirstAbility() {
        return p1_state.getPlayerFirstAbility();
    }

    protected Direction getCurrentPlayerGravityDirection() {
        return p1_state.getCurrentPlayerGravityDirection();
    }

    protected GravityStrength getPlayerGravityStrengthModifier() {
        return p1_state.getPlayerGravityStrengthModifier();
    }

    protected boolean getPlayerGravityModified() {
        return p1_state.getPlayerGravityModified();
    }

    protected boolean getIfPlayerInGravityField() {
        return p1_state.getIfPlayerInGravityField();
    }

    protected PlayerSecondAbilities getPlayerSecondAbility() {
        return p2_state.getPlayerSecondAbility();
    }

    protected boolean getPlayerXrayState() {
        return p2_state.getPlayerXrayState();
    }

    protected PlayerThirdAbilities getPlayerThirdAbility() {
        return p3_state.getPlayerThirdAbility();
    }

    protected int getPlayerThirdAbilityCooldownTicks() {
        return p3_state.getPlayerThirdAbilityCooldownTicks();
    }

    protected boolean getIfPlayerIsCarrying() {
        return p3_state.getIfPlayerIsCarrying();
    }


    protected static PlayerState getDefaultPlayerState() {
        return new PlayerState(P1PlayerState.getDefaultPlayerState(), P2PlayerState.getDefaultPlayerState(), P3PlayerState.getDefaultPlayerState());
    }

    protected PlayerState setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        return new PlayerState(p1_state.setPlayerFirstAbility(new_first_ability), p2_state, p3_state);
    }

    protected PlayerState setPlayerGravityDirection(Direction new_grav_dir) {
        return new PlayerState(p1_state.setPlayerGravityDirection(new_grav_dir), p2_state, p3_state);
    }

    protected PlayerState setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        return new PlayerState(p1_state.setPlayerGravityStrengthModifier(new_grav_strength), p2_state, p3_state);
    }

    protected PlayerState setPlayerGravityModified() {
        return new PlayerState(p1_state.setPlayerGravityModified(), p2_state, p3_state);
    }

    protected PlayerState setPlayerInGravityField(boolean in_field) {
        return new PlayerState(p1_state.setPlayerInGravityField(in_field), p2_state, p3_state);
    }

    protected PlayerState setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        return new PlayerState(p1_state, p2_state.setPlayerSecondAbility(new_second_ability), p3_state);
    }

    protected PlayerState setPlayerXrayState(boolean xray_on) {
        return new PlayerState(p1_state, p2_state.setPlayerXrayState(xray_on), p3_state);
    }

    protected PlayerState setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        return new PlayerState(p1_state, p2_state, p3_state.setPlayerThirdAbility(new_third_ability));
    }

    protected PlayerState setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        return new PlayerState(p1_state, p2_state, p3_state.setPlayerThirdAbilityCooldownTicks(new_third_ability_cooldown));
    }

    protected PlayerState decrementPlayerThirdAbilityCooldown() {
        return new PlayerState(p1_state, p2_state, p3_state.decrementPlayerThirdAbilityCooldown());
    }

    protected PlayerState setIfPlayerIsCarrying(boolean carrying) {
        return new PlayerState(p1_state, p2_state, p3_state.setPlayerIsCarrying(carrying));
    }


    protected PlayerState tick() {
        if (getPlayerThirdAbilityCooldownTicks() > 0) {
            return decrementPlayerThirdAbilityCooldown();
        } 
        return this;
    }


    protected static final Codec<PlayerState> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        P1PlayerState.CODEC.fieldOf("p1_state").forGetter(PlayerState::p1_state),
        P2PlayerState.CODEC.fieldOf("p2_state").forGetter(PlayerState::p2_state),
        P3PlayerState.CODEC.fieldOf("p3_state").forGetter(PlayerState::p3_state)
    ).apply(inst, PlayerState::new));
}
