package mcsoc.npcmod.entities.npc;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.datatypes.npcs.MovementInstruction;
import mcsoc.npcmod.util.InstructionReader;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class MovingNPC extends BaseNPC implements InstructionReader<MovementInstruction> {

    private Queue<MovementInstruction> movements_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;

    public MovingNPC(EntityType<? extends MovingNPC> entityType, World world) {
        super(entityType, world);
    }


    public void loadInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Walk(PositionData pos, double speed): {
                this.current_operation_ticks_remaining = 1;
                Vec3d target = pos.getPos();
                this.setAngles(pos.yaw(), pos.pitch());
                this.getNavigation().startMovingTo(target.x, target.y, target.z, speed);
                break;
            }
            case MovementInstruction.Swim(PositionData pos, double speed): {
                this.setSwimming(true);
                this.current_operation_ticks_remaining = 1;
                Vec3d target = pos.getPos();
                this.setAngles(pos.yaw(), pos.pitch());
                this.getNavigation().startMovingTo(target.x, target.y, target.z, speed);
                break;
            }
            case MovementInstruction.Turn(int ticks, float degrees): {
                this.current_operation_ticks_remaining = ticks;
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

    public void tickInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Walk(PositionData pos, double speed): {
                if (this.getNavigation().isFollowingPath()) {
                    this.current_operation_ticks_remaining = 1;
                }
                break;
            }
            case MovementInstruction.Swim(PositionData pos, double speed): {
                if (this.getNavigation().isFollowingPath()) {
                    this.current_operation_ticks_remaining = 1;
                }
                break;
            }
            case MovementInstruction.Turn(int ticks, float target_degrees): {
                float rotation_rate = (target_degrees - this.getYaw()) / ticks;
                this.setYaw(this.getYaw() + rotation_rate);
                
                Vec3d target = this.getEyePos().add(this.getRotationVector().multiply(2));
                this.lookAt(EntityAnchor.EYES, target);
                break;
            }
            case MovementInstruction.Jump(): {
                this.current_operation_ticks_remaining = 20;
                this.jumpControl.tick();
                break;
            }
            default:
        }
    }
    

    @Override
    public boolean shouldRepeat() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;

        this.tickReader();
    }


    @Override
    protected void initGoals() {
        // don't init goals
    }


    @Override
    public int getRemainingTicks() {
        return this.current_operation_ticks_remaining;
    }
    @Override
    public void decrementRemainingTicks() {
        this.current_operation_ticks_remaining--;
    }

    @Override
    public Queue<MovementInstruction> getInstructionQueue() {
        return this.movements_queue;
    }

    @Override
    public List<MovementInstruction> getNewInstructions() {
        return NpcModServerDataStorage.getInstance().getMovements(this).movements();
    }
}