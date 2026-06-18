package mcsoc.npcmod.util;

import java.util.List;
import java.util.Queue;


public interface InstructionReader<T extends Instruction> {

    abstract void loadInstruction(T instruction);

    abstract void tickInstruction(T instruction);

    abstract int getRemainingTicks();
    abstract void decrementRemainingTicks();
    abstract Queue<T> getInstructionQueue();
    abstract List<T> getNewInstructions();
    default boolean shouldRepeat() {
        return false;
    }

    public default boolean tickReader() {
        Queue<T> instruction_queue = this.getInstructionQueue();
        if (instruction_queue.isEmpty()) {
            instruction_queue.addAll(this.getNewInstructions());
            this.loadInstruction(instruction_queue.peek());
        }
        if (this.getRemainingTicks() == 0) {
            while (this.getRemainingTicks() == 0) {
                
                if (instruction_queue.isEmpty()) {
                    if (this.shouldRepeat()) {
                        instruction_queue.addAll(this.getNewInstructions());
                    } else {
                        break;
                    }
                }
                this.loadInstruction(instruction_queue.poll());
            }
        } else {
            this.decrementRemainingTicks();
            this.tickInstruction(instruction_queue.peek());
        }

        return !instruction_queue.isEmpty();
    }
}
