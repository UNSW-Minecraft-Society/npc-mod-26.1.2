package mcsoc.npcmod.entities.npc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;


public class BasicNPC extends BaseNPC {

    public BasicNPC(EntityType<? extends BasicNPC> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createNPCAttributes() {
      return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(100, new LookAtEntityGoal(this, PlayerEntity.class, 20.0F));
    }
}
