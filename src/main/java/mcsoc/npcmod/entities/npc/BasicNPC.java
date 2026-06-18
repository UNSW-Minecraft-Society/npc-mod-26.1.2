package mcsoc.npcmod.entities.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BasicNPC extends BaseNPC {

    public BasicNPC(EntityType<? extends BasicNPC> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createNPCAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(100, new LookAtPlayerGoal(this, Player.class, 20.0F));
    }
}
