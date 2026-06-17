package veveddo.npcmod.entities.camera;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.world.World;

public class CameraClientEntity extends MarkerEntity {

    public CameraClientEntity(EntityType<? extends CameraClientEntity> type, World world) {
        super(type, world);
    }

}
