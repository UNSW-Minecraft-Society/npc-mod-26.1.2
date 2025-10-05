package mcsoc.planetgame;

import mcsoc.planetgame.networking.SyncPlayerGravityDataS2CPayload;
import net.minecraft.util.math.Direction;

public record ClientPlayerState(Direction grav_dir, boolean xray_active) {

    public Direction getPlayerGravityDirection() {
        return this.grav_dir;
    }

    private ClientPlayerState withGravityDirection(Direction grav_dir) {
        return new ClientPlayerState(grav_dir, getXrayState());
    }

    public boolean getXrayState() {
        return this.xray_active;
    }

    private ClientPlayerState withXrayState(boolean xray_active) {
        return new ClientPlayerState(getPlayerGravityDirection(), xray_active);
    }

    public static ClientPlayerState getDefaultPlayerState() {
        return new ClientPlayerState(Direction.DOWN, false);
    }

    public ClientPlayerState updateGravityFromPayload(SyncPlayerGravityDataS2CPayload payload) {
        return this.withGravityDirection(payload.gravity_direction());
    }
    
}
