package mcsoc.npcmod.cutscenes;

import java.util.ArrayDeque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;

import mcsoc.npcmod.NpcMod;
import mcsoc.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.datatypes.cutscenes.CameraMode;
import mcsoc.npcmod.datatypes.cutscenes.CutsceneInstruction;
import mcsoc.npcmod.entities.EntityRegistration;
import mcsoc.npcmod.entities.npc.BaseNPC;
import mcsoc.npcmod.entities.npc.BasicNPC;
import mcsoc.npcmod.entities.npc.MovingNPC;
import mcsoc.npcmod.networking.CameraPanS2CPayload;
import mcsoc.npcmod.networking.SyncCameraModeS2CPayload;
import mcsoc.npcmod.networking.SyncCameraPositionS2CPayload;
import mcsoc.npcmod.util.InstructionReader;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;


public class CutsceneHandler implements InstructionReader<CutsceneInstruction> {

    private static final CutsceneHandler INSTANCE = new CutsceneHandler();

    private Queue<CutsceneInstruction> actions_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;
    private boolean should_play = false;
    private Optional<String> cutscene_id = Optional.empty();
    private Optional<ServerWorld> world = Optional.empty();
    private PositionData call_position;


    private CutsceneHandler() {}
    public static CutsceneHandler getInstance() {
        return INSTANCE;
    }

    public void loadCutscene(String cutscene_id, Vec3d call_position, Vec2f call_rotation) {
        if (world.isEmpty()) throw new NoSuchElementException("world was not initialised");

        this.cutscene_id = Optional.of(cutscene_id);
        this.actions_queue.clear();
        this.actions_queue.addAll(this.getNewInstructions());
        this.call_position = PositionData.fromPosAndAngles(call_position, call_rotation.y, call_rotation.x);
    }

    public void setWorld(ServerWorld world) {
        this.world = Optional.of(world);
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
        ServerWorld loaded_world = this.world.orElseThrow();

        switch (instruction) {
            case CutsceneInstruction.SpawnNPC(String npc_id, PositionData position_data) -> {
                BasicNPC npc = new BasicNPC(EntityRegistration.BASIC_NPC, loaded_world);
                applyNPCAttributes(npc, npc_id, call_position.add(position_data));
                loaded_world.spawnEntity(npc);
            }
            case CutsceneInstruction.SpawnMovingNPC(String npc_id, PositionData position_data) -> {
                MovingNPC npc = new MovingNPC(EntityRegistration.MOVING_NPC, loaded_world);
                applyNPCAttributes(npc, npc_id, call_position.add(position_data));
                loaded_world.spawnEntity(npc);
            }
            case CutsceneInstruction.Dialogue(Text text) -> {
                loaded_world.getPlayers().forEach(player -> player.sendMessage(text));
            }
            case CutsceneInstruction.Delay(int ticks) -> {
                this.current_operation_ticks_remaining = ticks;
            }
            case CutsceneInstruction.PositionCamera(PositionData position_data) -> {
                loaded_world.getPlayers().forEach(player -> 
                    ServerPlayNetworking.send(player, new SyncCameraPositionS2CPayload(call_position.add(position_data)))
                );
            }
            case CutsceneInstruction.SetCameraMode(CameraMode mode) -> {
                loaded_world.getPlayers().forEach(player -> {
                    ServerPlayNetworking.send(player, new SyncCameraModeS2CPayload(mode));
                });
            }
            case CutsceneInstruction.PanCamera(PositionData from, PositionData to, int ticks) -> {
                loaded_world.getPlayers().forEach(player -> {
                    ServerPlayNetworking.send(player, new CameraPanS2CPayload(call_position.add(from), call_position.add(to), ticks));
                });
                this.current_operation_ticks_remaining = ticks;
            }
            case CutsceneInstruction.PlaySound(Identifier sound_id, int x, int y, int z) -> {
                loaded_world.playSound(null, x, y, z, SoundEvent.of(sound_id), SoundCategory.PLAYERS, 1, 1);
            }
            case CutsceneInstruction.PlaySoundPlayers(Identifier sound_id) -> {
                loaded_world.getPlayers().forEach(player -> 
                    loaded_world.playSound(player, player.getBlockPos(), SoundEvent.of(sound_id, 20), SoundCategory.PLAYERS, 1, 1)
                );
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
        if (!this.should_play || world.isEmpty()) {
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
        return NpcModServerDataStorage.getInstance().getCutscene(this.cutscene_id.orElseThrow()).actions();
    }
}
