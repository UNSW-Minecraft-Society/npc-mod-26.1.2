package mcsoc.npcmod.entities;


import mcsoc.npcmod.NpcMod;
import mcsoc.npcmod.entities.camera.CameraClientEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ClientEntityRegistration {
    private ClientEntityRegistration() { /* delete */ }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, name), builder.build());
    }

    public static final EntityType<CameraClientEntity> CAMERA = register(
        "camera",
        EntityType.Builder.of(CameraClientEntity::new, MobCategory.MISC)
            .alwaysUpdateVelocity(false)
    );

    public static void registerEntities() {
        // init statics
    }
}
