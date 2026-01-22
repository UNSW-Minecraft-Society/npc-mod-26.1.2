package mcsoc.npcmod.entities.camera;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.world.World;

public class CameraClientEntity extends MarkerEntity {

    public CameraClientEntity(EntityType<? extends CameraClientEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();

        this.lastRenderX = this.getX();
        this.lastRenderY = this.getY();
        this.lastRenderZ = this.getZ();
    }
}
