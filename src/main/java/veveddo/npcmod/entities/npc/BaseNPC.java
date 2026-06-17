package veveddo.npcmod.entities.npc;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;

public abstract class BaseNPC extends PathAwareEntity implements VillagerDataContainer {

    public static final TrackedData<String> NPC_ID = DataTracker.registerData(BaseNPC.class, TrackedDataHandlerRegistry.STRING);
    private boolean has_set_name = false;

    protected BaseNPC(EntityType<? extends BaseNPC> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
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
    public boolean isAttackable() {
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
