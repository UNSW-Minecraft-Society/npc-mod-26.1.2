package veveddo.npcmod.entities;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import veveddo.npcmod.NpcMod;
import veveddo.npcmod.entities.camera.CameraClientEntity;

public class ClientEntityRegistration {
    private ClientEntityRegistration() { /* delete */ }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(NpcMod.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
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
