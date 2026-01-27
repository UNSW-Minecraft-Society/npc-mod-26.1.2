package mcsoc.npcmod.entities;

import mcsoc.npcmod.NpcMod;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ClientEntityRegistration {
    private ClientEntityRegistration() { /* delete */ }

    public static final EntityType<CameraClientEntity> CAMERA = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(NpcMod.MOD_ID, "camera"),
        EntityType.Builder.create(CameraClientEntity::new, SpawnGroup.MISC)
        .alwaysUpdateVelocity(false)
        .build("camera")
    );

    public static void registerEntities() {
        // init statics
    }
}
