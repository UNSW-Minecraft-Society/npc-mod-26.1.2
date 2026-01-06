package mcsoc.npcmod.entities.npc;

import mcsoc.npcmod.dataloader.datastorage.NPCServerDataLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;

public abstract class BaseNPC extends PathAwareEntity implements VillagerDataContainer {

    protected static final TrackedData<String> NPC_ID = DataTracker.registerData(BaseNPC.class, TrackedDataHandlerRegistry.STRING);

    protected BaseNPC(EntityType<? extends BaseNPC> entityType, World world) {
        super(entityType, world);
    }


    @Override
    protected void initDataTracker(Builder builder) {
        super.initDataTracker(builder);
        builder.add(NPC_ID, "");
    }

    public String getID() {
        return this.dataTracker.get(NPC_ID);
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    @Override
    public Text getCustomName() {
        return NPCServerDataLoader.getInstance().getDialogue(this).display_name();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("npc_id", this.dataTracker.get(NPC_ID));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(NPC_ID, nbt.getString("npc_id"));
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }
    
    @Override
    public VillagerData getVillagerData() {
        return new VillagerData(VillagerType.DESERT, VillagerProfession.CLERIC, 1);
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        // no-op
    }
}
