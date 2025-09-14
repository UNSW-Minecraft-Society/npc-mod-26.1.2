package mcsoc.planetgame;

import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import net.minecraft.util.math.Direction;

public record ClientPlayerState(Direction grav_dir) {

    public ClientPlayerState setPlayerGravDirection(Direction new_grav_dir) {
        return new ClientPlayerState(new_grav_dir);
    }

    public Direction getPlayerGravDirection() {
        return this.grav_dir;
    }

    public static ClientPlayerState getDefaultPlayerState() {
        return new ClientPlayerState(Direction.DOWN);
    }

    public ClientPlayerState updateGravityFromPayload(SyncPlayerGravityDataS2CPayload payload) {
        return new ClientPlayerState(payload.gravity_direction());
    }
    
}
