package mcsoc.npcmod.entities.npc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import mcsoc.npcmod.NPCMod;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.EntityType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MovingNPC extends BasicNPC {

    private static final int TURN_DEGREES_PER_TICK = 9;

    public sealed interface MovementInstruction {
        record Walk(int ticks, double speed) implements MovementInstruction {}
        record Swim(int ticks, double speed) implements MovementInstruction {}
        record Turn(int ticks, double degrees) implements MovementInstruction {}
        record Stop(int ticks) implements MovementInstruction {}
        record Jump() implements MovementInstruction {}
    }

    private final List<MovementInstruction> movements_list = new ArrayList<>();
    private Queue<MovementInstruction> movements_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;

    public MovingNPC(EntityType<? extends BasicNPC> entityType, World world) {
        super(entityType, world);
        movements_list.add(new MovementInstruction.Walk(40, 0.3));
        movements_list.add(new MovementInstruction.Stop(5));
        movements_list.add(new MovementInstruction.Jump());
        movements_list.add(new MovementInstruction.Turn(20, 180));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient) return;

        if (current_operation_ticks_remaining <= 0) {
            movements_queue.poll();
            if (movements_queue.isEmpty()) {
                movements_queue.addAll(movements_list);
            }
            MovementInstruction instruction = movements_queue.peek();
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
            }
        } else {
            current_operation_ticks_remaining--;
            switch (this.movements_queue.peek()) {
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
    }


    @Override
    protected void initGoals() {
    }
}