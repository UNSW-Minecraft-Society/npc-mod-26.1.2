package mcsoc.planetgame.statemanagement.playerstate;

import com.mojang.serialization.Codec;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import net.minecraft.util.math.Direction;

public class ManagedPlayerState {

    private PlayerState state;

    ManagedPlayerState(PlayerState state) {
        this.state = state;
    }

    ManagedPlayerState() {
        this(PlayerState.withDefaultPlayerState());
    }

    private PlayerState getState() {
        return state;
    }


    public static ManagedPlayerState getDefaultPlayerState() {
        return new ManagedPlayerState();
    }


    public PlayerFirstAbilities getPlayerFirstAbility() {
        return state.getPlayerFirstAbility();
    }

    public Direction getCurrentPlayerGravityDirection() {
        return state.getCurrentPlayerGravityDirection();
    }

    public GravityStrength getPlayerGravityStrengthModifier() {
        return state.getPlayerGravityStrengthModifier();
    }

    public boolean getPlayerGravityModified() {
        return state.getPlayerGravityModified();
    }

    public boolean getIfPlayerInGravityField() {
        return state.getIfPlayerInGravityField();
    }

    public PlayerSecondAbilities getPlayerSecondAbility() {
        return state.getPlayerSecondAbility();
    }

    public boolean getPlayerXrayState() {
        return state.getPlayerXrayState();
    }

    public PlayerThirdAbilities getPlayerThirdAbility() {
        return state.getPlayerThirdAbility();
    }

    public int getPlayerThirdAbilityCooldownTicks() {
        return state.getPlayerThirdAbilityCooldownTicks();
    }

    public boolean getIfPlayerIsCarrying() {
        return state.getIfPlayerIsCarrying();
    }


    public void setPlayerFirstAbility(PlayerFirstAbilities new_first_ability) {
        this.state = state.withPlayerFirstAbility(new_first_ability);
    }

    public void setPlayerGravityDirection(Direction new_grav_dir) {
        this.state = state.withPlayerGravityDirection(new_grav_dir);
    }

    public void setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        this.state = state.withPlayerGravityStrengthModifier(new_grav_strength);
    }

    public void setPlayerGravityModified() {
        this.state = state.withPlayerGravityModified();
    }

    public void setPlayerInGravityField(boolean in_field) {
        this.state = state.withPlayerInGravityField(in_field);
    }

    public void setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        this.state = state.withPlayerSecondAbility(new_second_ability);
    }

    public void setPlayerXrayState(boolean xray_on) {
        this.state = state.setPlayerXrayState(xray_on);
    }

    public void setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        this.state = state.withPlayerThirdAbility(new_third_ability);
    }

    public void setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        this.state = state.withPlayerThirdAbilityCooldownTicks(new_third_ability_cooldown);
    }

    public void decrementPlayerThirdAbilityCooldown() {
        this.state = state.withDecrementedPlayerThirdAbilityCooldown();
    }

    public void setIfPlayerIsCarrying(boolean carrying) {
        this.state = state.withIsPlayerCarrying(carrying);
    }


    public void tick() {
        this.state = state.tick();
    }

    public static final Codec<ManagedPlayerState> CODEC = PlayerState.CODEC.xmap(ManagedPlayerState::new, ManagedPlayerState::getState);
}
