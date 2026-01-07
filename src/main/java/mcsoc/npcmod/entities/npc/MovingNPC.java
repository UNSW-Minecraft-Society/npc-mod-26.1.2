package mcsoc.npcmod.entities.npc;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementInstruction;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class MovingNPC extends BaseNPC {

    private Queue<MovementInstruction> movements_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;

    public MovingNPC(EntityType<? extends MovingNPC> entityType, World world) {
        super(entityType, world);
    }


    protected void loadInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Walk(int ticks, double speed): {
                this.current_operation_ticks_remaining = ticks;
                Vec3d target = this.getPos().add(this.getRotationVector().multiply(20));
                this.lookAt(EntityAnchor.EYES, target);
                this.getNavigation().startMovingTo(target.x, target.y, target.z, speed);
                break;
            }
            case MovementInstruction.Turn(int ticks, double degrees): {
                this.current_operation_ticks_remaining = ticks;
                break;
            }
            case MovementInstruction.Swim(int ticks, double speed): {
                this.current_operation_ticks_remaining = ticks;
                Vec3d target = this.getPos().add(this.getRotationVector().multiply(20));
                this.getNavigation().startMovingTo(target.x, target.y, target.z, speed);
                this.setSwimming(true);
                break;
            }
            case MovementInstruction.Stop(int ticks): {
                this.current_operation_ticks_remaining = ticks;
                this.setSwimming(false);
                this.getNavigation().stop();
                break;
            }
            case MovementInstruction.Jump(): {
                this.current_operation_ticks_remaining = 20;
                this.jumpControl.setActive();
                break;
            }
            default:
        }
    }

    protected void tickInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Turn(int ticks, double degrees): {
                float rotation_rate = (float)degrees / ticks;
                this.setYaw(this.getYaw() + rotation_rate);
                
                Vec3d target = this.getEyePos().add(this.getRotationVector().multiply(2));
                this.lookAt(EntityAnchor.EYES, target);
                break;
            }
            default:
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;

        if (current_operation_ticks_remaining == 0) {
            this.movements_queue.poll();

            if (this.movements_queue.isEmpty()) {
                this.movements_queue.addAll(NPCServerDataLoader.getInstance().getMovements(this).movements());
            }
            this.loadInstruction(this.movements_queue.peek());
        } else {
            current_operation_ticks_remaining--;
            this.tickInstruction(this.movements_queue.peek());
        }
    }


    @Override
    protected void initGoals() {
        // don't init goals
    }
}