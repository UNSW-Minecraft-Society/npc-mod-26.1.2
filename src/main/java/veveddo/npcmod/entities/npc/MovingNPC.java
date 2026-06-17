package veveddo.npcmod.entities.npc;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.datatypes.npcs.MovementInstruction;
import veveddo.npcmod.util.InstructionReader;


public class MovingNPC extends BaseNPC implements InstructionReader<MovementInstruction> {

    private Queue<MovementInstruction> movements_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;

    public MovingNPC(EntityType<? extends MovingNPC> entityType, Level world) {
        super(entityType, world);
    }


    public void loadInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Walk(PositionData pos, double speed): {
                this.current_operation_ticks_remaining = 1;
                Vec3 target = pos.getPos();
                this.absSnapRotationTo(pos.yaw(), pos.pitch());
                this.getNavigation().moveTo(target.x, target.y, target.z, speed);
                break;
            }
            case MovementInstruction.Swim(PositionData pos, double speed): {
                this.setSwimming(true);
                this.current_operation_ticks_remaining = 1;
                Vec3 target = pos.getPos();
                this.absSnapRotationTo(pos.yaw(), pos.pitch());
                this.getNavigation().moveTo(target.x, target.y, target.z, speed);
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
                this.jumpControl.jump();
                break;
            }
            default:
        }
    }

    public void tickInstruction(MovementInstruction instruction) {
        switch (instruction) {
            case MovementInstruction.Walk(PositionData pos, double speed): {
                if (this.getNavigation().isInProgress()) {
                    this.current_operation_ticks_remaining = 1;
                }
                break;
            }
            case MovementInstruction.Swim(PositionData pos, double speed): {
                if (this.getNavigation().isInProgress()) {
                    this.current_operation_ticks_remaining = 1;
                }
                break;
            }
            case MovementInstruction.Turn(int ticks, float target_degrees): {
                float rotation_rate = (target_degrees - this.getYRot()) / ticks;
                this.setYRot(this.getYRot() + rotation_rate);
                
                Vec3 target = this.getEyePosition().add(this.getLookAngle().scale(2));
                this.lookAt(Anchor.EYES, target);
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
        if (this.level().isClientSide()) return;

        this.tickReader();
    }


    @Override
    protected void registerGoals() {
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