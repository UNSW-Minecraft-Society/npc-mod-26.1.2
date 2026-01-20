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
import mcsoc.npcmod.networking.SyncCameraModeS2CPayload;
import mcsoc.npcmod.networking.SyncCameraPositionS2CPayload;
import mcsoc.npcmod.util.InstructionReader;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;


public class CutsceneHandler implements InstructionReader<CutsceneInstruction> {

    private static final CutsceneHandler INSTANCE = new CutsceneHandler();

    private String cutscene_id = "";
    private Queue<CutsceneInstruction> actions_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;
    private ServerWorld world = null;
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

    public void setWorld(ServerWorld world) {
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


    private static BaseNPC applyNPCAttributes(BaseNPC npc, String npc_id, PositionData position_data) {
        npc.setPosition(position_data.x(), position_data.y(), position_data.z());
        npc.setRotation(position_data.yaw(), position_data.pitch());
        npc.getDataTracker().set(BaseNPC.NPC_ID, npc_id);
        return npc;
    }


    public void loadInstruction(CutsceneInstruction instruction) {
        switch (instruction) {
            case CutsceneInstruction.SpawnNPC(String npc_id, PositionData position_data) -> {
                BasicNPC npc = new BasicNPC(EntityRegistration.BASIC_NPC, this.world);
                applyNPCAttributes(npc, npc_id, position_data);
                this.world.spawnEntity(npc);
            }
            case CutsceneInstruction.SpawnMovingNPC(String npc_id, PositionData position_data) -> {
                MovingNPC npc = new MovingNPC(EntityRegistration.MOVING_NPC, this.world);
                applyNPCAttributes(npc, npc_id, position_data);
                this.world.spawnEntity(npc);
            }
            case CutsceneInstruction.Dialogue(Text text) -> {
                this.world.getPlayers().forEach(player -> player.sendMessage(text));
            }
            case CutsceneInstruction.Delay(int ticks) -> {
                this.current_operation_ticks_remaining = ticks;
            }
            case CutsceneInstruction.PositionCamera(PositionData position_data) -> {
                this.world.getPlayers().forEach(player -> {
                    ServerPlayNetworking.send(player, new SyncCameraPositionS2CPayload(position_data));
                });
            }
            case CutsceneInstruction.SetCameraMode(CameraMode mode) -> {
                this.world.getPlayers().forEach(player -> {
                    ServerPlayNetworking.send(player, new SyncCameraModeS2CPayload(mode));
                });
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
