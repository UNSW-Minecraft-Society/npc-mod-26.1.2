package veveddo.npcmod.entities.camera;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.Level;

public class CameraClientEntity extends Marker {

    public CameraClientEntity(EntityType<? extends CameraClientEntity> type, Level world) {
        super(type, world);
    }
}
