package mcsoc.planetgame.statemanagement.playerstate;

import com.mojang.serialization.Codec;

import mcsoc.planetgame.statemanagement.enums.GravityStrength;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerFirstAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerSecondAbilities;
import mcsoc.planetgame.statemanagement.enums.playerabilities.PlayerThirdAbilities;
import net.minecraft.util.math.Direction;

public class PlayerStateManager {

    private PlayerState state;

    PlayerStateManager(PlayerState state) {
        this.state = state;
    }

    PlayerStateManager() {
        this(PlayerState.getDefaultPlayerState());
    }

    private PlayerState getState() {
        return state;
    }


    public static PlayerStateManager getDefaultPlayerState() {
        return new PlayerStateManager();
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
        this.state = state.setPlayerFirstAbility(new_first_ability);
    }

    public void setPlayerGravityDirection(Direction new_grav_dir) {
        this.state = state.setPlayerGravityDirection(new_grav_dir);
    }

    public void setPlayerGravityStrengthModifier(GravityStrength new_grav_strength) {
        this.state = state.setPlayerGravityStrengthModifier(new_grav_strength);
    }

    public void setPlayerGravityModified() {
        this.state = state.setPlayerGravityModified();
    }

    public void setPlayerInGravityField(boolean in_field) {
        this.state = state.setPlayerInGravityField(in_field);
    }

    public void setPlayerSecondAbility(PlayerSecondAbilities new_second_ability) {
        this.state = state.setPlayerSecondAbility(new_second_ability);
    }

    public void setPlayerThirdAbility(PlayerThirdAbilities new_third_ability) {
        this.state = state.setPlayerThirdAbility(new_third_ability);
    }

    public void setPlayerThirdAbilityCooldownTicks(int new_third_ability_cooldown) {
        this.state = state.setPlayerThirdAbilityCooldownTicks(new_third_ability_cooldown);
    }

    public void decrementPlayerThirdAbilityCooldown() {
        this.state = state.decrementPlayerThirdAbilityCooldown();
    }

    public void setIfPlayerIsCarrying(boolean carrying) {
        this.state = state.setIfPlayerIsCarrying(carrying);
    }


    public void tick() {
        this.state = state.tick();
    }

    public static final Codec<PlayerStateManager> CODEC = PlayerState.CODEC.xmap(PlayerStateManager::new, PlayerStateManager::getState);
}
