package mcsoc.npcmod.cutscenes;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import mcsoc.npcmod.NpcMod;
import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.datatypes.cutscenes.CutsceneInstruction;
import mcsoc.npcmod.datatypes.cutscenes.PositionData;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.BaseNPC;
import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.MovingNPC;
import mcsoc.npcmod.util.InstructionReader;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CutsceneHandler implements InstructionReader<CutsceneInstruction> {

    private static final CutsceneHandler INSTANCE = new CutsceneHandler();

    private String cutscene_id = "";
    private Queue<CutsceneInstruction> actions_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;
    private World world = null;
    private boolean should_play = false;

    private CutsceneHandler() {}
    public static CutsceneHandler getInstance() {
        return INSTANCE;
    }


    public void loadCutscene(String cutscene_id) {
        this.cutscene_id = cutscene_id;
        this.actions_queue.clear();
        this.actions_queue.addAll(this.getNewInstructions());
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void start() {
        NpcMod.LOGGER.info("started");
        this.should_play = true;
    }

    public void stop() {
        NpcMod.LOGGER.info("stopped");
        this.should_play = false;
    }


    private static BaseNPC applyNpcAttributes(BaseNPC npc, String npc_id, BlockPos position, FacingData facing) {
        npc.setPosition(position.toCenterPos());
        npc.setRotation(facing.yaw(), facing.pitch());
        npc.getDataTracker().set(BaseNPC.NPC_ID, npc_id);
        return npc;
    }


    public void loadInstruction(CutsceneInstruction instruction) {
        switch (instruction) {
            case CutsceneInstruction.SpawnNPC(String npc_id, BlockPos position, FacingData facing) -> {
                BasicNPC npc = new BasicNPC(EntityRegistration.BASIC_NPC, this.world);
                applyNpcAttributes(npc, npc_id, position, facing);
                this.world.spawnEntity(npc);
            }
            case CutsceneInstruction.SpawnMovingNPC(String npc_id, BlockPos position, FacingData facing) -> {
                MovingNPC npc = new MovingNPC(EntityRegistration.MOVING_NPC, this.world);
                applyNpcAttributes(npc, npc_id, position, facing);
                this.world.spawnEntity(npc);
            }
            case CutsceneInstruction.Dialogue(Text text) -> {
                this.world.getPlayers().forEach(player -> player.sendMessage(text));
            }
            case CutsceneInstruction.Delay(int ticks) -> {
                this.current_operation_ticks_remaining = ticks;
            }
            default -> {}
        }
    }

    public void tickInstruction(CutsceneInstruction instruction) {
        switch (instruction) {
            default:
        }
    }

    @Override
    public boolean tickReader() {
        if (!this.should_play || Objects.isNull(world)) {
            return false;
        }
        if (!InstructionReader.super.tickReader()) {
            this.stop();
            return false;
        }
        return true;
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
    public Queue<CutsceneInstruction> getInstructionQueue() {
        return this.actions_queue;
    }

    @Override
    public List<CutsceneInstruction> getNewInstructions() {
        return NpcModServerDataStorage.getInstance().getCutscene(this.cutscene_id).actions();
    }
}
