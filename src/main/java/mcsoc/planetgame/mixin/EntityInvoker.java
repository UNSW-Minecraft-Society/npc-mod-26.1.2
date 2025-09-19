package mcsoc.planetgame.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("scheduleVelocityUpdate")
    public void invokeScheduleVelocityUpdate();

    @Accessor
    public Vec3d getVelocity();

    @Accessor
    public void setVelocity(Vec3d velocity);

    @Accessor
    public RemovalReason getRemovalReason();

}
