package mcsoc.npcmod.entities.npc;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public abstract class BaseNPC extends PathfinderMob {

    public static final EntityDataAccessor<String> NPC_ID = SynchedEntityData.defineId(BaseNPC.class, EntityDataSerializers.STRING);
    private boolean has_set_name = false;

    protected BaseNPC(EntityType<? extends BaseNPC> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(NPC_ID, "");
    }

    public String getID() {
        return this.getEntityData().get(NPC_ID);
    }

    private void setID(String s) {
        this.getEntityData().set(NPC_ID, s);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("npc_id", this.getID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setID(nbt.getString("npc_id"));
    }

    @Override
    public void tick() {
        super.tick();
        var data_store = NpcModServerDataStorage.getInstance();
        this.setCustomName(data_store.getDialogue(this).display_name());
        if (!this.has_set_name || data_store.hasUpdated()) {
            this.setCustomName(data_store.getDialogue(this).display_name());
            this.has_set_name = true;
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    @Override
    public boolean isAttackable() {
        return false;
    }
}
