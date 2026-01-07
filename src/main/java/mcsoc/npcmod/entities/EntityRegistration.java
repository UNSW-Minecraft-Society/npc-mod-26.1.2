package mcsoc.npcmod.entities;

import mcsoc.npcmod.NpcMod;
import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.MovingNPC;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class EntityRegistration {
    private EntityRegistration() { /* delete */ }

    public static final EntityType<BasicNPC> BASIC_NPC = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(NpcMod.MOD_ID, "basic_npc"),
        EntityType.Builder.create(BasicNPC::new, SpawnGroup.CREATURE)
        .alwaysUpdateVelocity(false)
        .build("basic_npc")
    );

    public static final EntityType<MovingNPC> MOVING_NPC = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(NpcMod.MOD_ID, "moving_npc"),
        EntityType.Builder.create(MovingNPC::new, SpawnGroup.CREATURE)
        .alwaysUpdateVelocity(true)
        .build("moving_npc")
    );

    
    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BASIC_NPC, BasicNPC.createNPCAttributes());
        FabricDefaultAttributeRegistry.register(MOVING_NPC, BasicNPC.createNPCAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.7));
    }
}
