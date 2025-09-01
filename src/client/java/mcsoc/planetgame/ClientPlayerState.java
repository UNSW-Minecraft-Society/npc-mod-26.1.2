package mcsoc.planetgame;

import mcsoc.planetgame.StateManagement.PlayerState;
import net.minecraft.util.math.Direction;

public record ClientPlayerState(Double grav_mod, Direction grav_dir) {

    public static ClientPlayerState fromPlayerState(PlayerState ps) {
        return new ClientPlayerState(ps.grav_mod(), ps.grav_dir());
    }

    public ClientPlayerState setPlayerGravStrengthModifier(Double new_grav_mod) {
        return new ClientPlayerState(new_grav_mod, grav_dir);
    }

    public Double getPlayerGravStrengthModifier() {
        return grav_mod;
    }

    public ClientPlayerState setPlayerGravDirection(Direction new_grav_dir) {
        return new ClientPlayerState(grav_mod, new_grav_dir);
    }

    public Direction getPlayerGravDirection() {
        return grav_dir;
    }


    public static ClientPlayerState getDefaultPlayerState() {
        return new ClientPlayerState(Double.valueOf(1), Direction.DOWN);
    }
    
}
