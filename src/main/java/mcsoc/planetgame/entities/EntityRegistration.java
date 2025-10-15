package mcsoc.planetgame.entities;

import mcsoc.planetgame.PlanetGame;
import mcsoc.planetgame.entities.throwables.ThrowableRockEntity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class EntityRegistration {
    private EntityRegistration() { /* delete */ }

    public static final EntityType<ThrowableRockEntity> ROCK = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(PlanetGame.MOD_ID, "throwable_rock"),
        EntityType.Builder.create(ThrowableRockEntity::new, SpawnGroup.CREATURE)
                .dimensions(1f, 1f)
                .makeFireImmune()
                .alwaysUpdateVelocity(true)
        .build("throwable_rock")
    );

    
    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(ROCK, LivingEntity.createLivingAttributes());
    }
}
