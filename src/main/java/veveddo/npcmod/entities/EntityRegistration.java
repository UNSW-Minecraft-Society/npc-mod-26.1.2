package veveddo.npcmod.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import veveddo.npcmod.NpcMod;
import veveddo.npcmod.entities.npc.BasicNPC;
import veveddo.npcmod.entities.npc.MovingNPC;

public abstract class EntityRegistration {
    private EntityRegistration() { /* delete */ }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(NpcMod.MOD_ID, name), builder.build());
    }

    public static final EntityType<BasicNPC> BASIC_NPC = register(
        "basic_npc", 
        EntityType.Builder.of(BasicNPC::new, MobCategory.CREATURE)
            .alwaysUpdateVelocity(false)
    );

    public static final EntityType<MovingNPC> MOVING_NPC = register(
        "moving_npc", 
        EntityType.Builder.of(MovingNPC::new, MobCategory.CREATURE)
            .alwaysUpdateVelocity(true)
    );

    
    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BASIC_NPC, BasicNPC.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(MOVING_NPC, BasicNPC.createNPCAttributes().add(Attributes.MOVEMENT_SPEED, 0.7));
    }
}
