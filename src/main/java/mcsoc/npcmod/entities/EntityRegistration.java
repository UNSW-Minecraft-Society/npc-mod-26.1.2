package mcsoc.npcmod.entities;

import mcsoc.npcmod.NPCMod;
import mcsoc.npcmod.entities.npc.BasicNPC;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class EntityRegistration {
    private EntityRegistration() { /* delete */ }

    public static final EntityType<BasicNPC> BASIC_NPC = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(NPCMod.MOD_ID, "basic_npc"),
        EntityType.Builder.create(BasicNPC::new, SpawnGroup.CREATURE)
        .build("basic_npc")
    );

    
    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(BASIC_NPC, BasicNPC.createNPCAttributes());
    }
}
