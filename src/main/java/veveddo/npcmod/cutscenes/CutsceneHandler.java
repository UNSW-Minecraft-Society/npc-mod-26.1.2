package veveddo.npcmod.cutscenes;

import java.util.ArrayDeque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import veveddo.npcmod.NpcMod;
import veveddo.npcmod.dataloader.datastorage.NpcModServerDataStorage;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.datatypes.cutscenes.CameraMode;
import veveddo.npcmod.datatypes.cutscenes.CutsceneInstruction;
import veveddo.npcmod.entities.EntityRegistration;
import veveddo.npcmod.entities.npc.BaseNPC;
import veveddo.npcmod.entities.npc.BasicNPC;
import veveddo.npcmod.entities.npc.MovingNPC;
import veveddo.npcmod.networking.CameraPanS2CPayload;
import veveddo.npcmod.networking.SyncCameraModeS2CPayload;
import veveddo.npcmod.networking.SyncCameraPositionS2CPayload;
import veveddo.npcmod.util.InstructionReader;


public class CutsceneHandler implements InstructionReader<CutsceneInstruction> {

    private static final CutsceneHandler INSTANCE = new CutsceneHandler();

    private Queue<CutsceneInstruction> actions_queue = new ArrayDeque<>();
    private int current_operation_ticks_remaining = 0;
    private boolean should_play = false;
    private Optional<String> cutscene_id = Optional.empty();
    private Optional<ServerLevel> world = Optional.empty();
    private PositionData call_position;


    private CutsceneHandler() {}
    public static CutsceneHandler getInstance() {
        return INSTANCE;
    }

    public void loadCutscene(String cutscene_id, Vec3 call_position, Vec2 call_rotation) {
        if (world.isEmpty()) throw new NoSuchElementException("world was not initialised");

        this.cutscene_id = Optional.of(cutscene_id);
        this.actions_queue.clear();
        this.actions_queue.addAll(this.getNewInstructions());
        this.call_position = PositionData.fromPosAndAngles(call_position, call_rotation.y, call_rotation.x);
    }

    public void setWorld(ServerLevel world) {
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
        npc.setPos(position_data.x(), position_data.y(), position_data.z());
        npc.setYRot(position_data.yaw());
        npc.setXRot(position_data.pitch());
        npc.getEntityData().set(BaseNPC.NPC_ID, npc_id);
        return npc;
    }


    public void loadInstruction(CutsceneInstruction instruction) {
        ServerLevel loaded_world = this.world.orElseThrow();

        switch (instruction) {
            case CutsceneInstruction.SpawnNPC(String npc_id, PositionData position_data) -> {
                BasicNPC npc = new BasicNPC(EntityRegistration.BASIC_NPC, loaded_world);
                applyNPCAttributes(npc, npc_id, call_position.add(position_data));
                loaded_world.addFreshEntity(npc);
            }
            case CutsceneInstruction.SpawnMovingNPC(String npc_id, PositionData position_data) -> {
                MovingNPC npc = new MovingNPC(EntityRegistration.MOVING_NPC, loaded_world);
                applyNPCAttributes(npc, npc_id, call_position.add(position_data));
                loaded_world.addFreshEntity(npc);
            }
            case CutsceneInstruction.Dialogue(Component text) -> {
                loaded_world.players().forEach(player -> player.sendSystemMessage(text));
            }
            case CutsceneInstruction.Delay(int ticks) -> {
                this.current_operation_ticks_remaining = ticks;
            }
            case CutsceneInstruction.PositionCamera(PositionData position_data) -> {
                loaded_world.players().forEach(player -> 
                    ServerPlayNetworking.send(player, new SyncCameraPositionS2CPayload(call_position.add(position_data)))
                );
            }
            case CutsceneInstruction.SetCameraMode(CameraMode mode) -> {
                loaded_world.players().forEach(player -> {
                    ServerPlayNetworking.send(player, new SyncCameraModeS2CPayload(mode));
                });
            }
            case CutsceneInstruction.PanCamera(PositionData from, PositionData to, int ticks) -> {
                loaded_world.players().forEach(player -> {
                    ServerPlayNetworking.send(player, new CameraPanS2CPayload(call_position.add(from), call_position.add(to), ticks));
                });
                this.current_operation_ticks_remaining = ticks;
            }
            case CutsceneInstruction.PlaySound(Identifier sound_id, int x, int y, int z) -> {
                loaded_world.playSound(null, x, y, z, SoundEvent.createVariableRangeEvent(sound_id), SoundSource.PLAYERS, 1, 1);
            }
            case CutsceneInstruction.PlaySoundPlayers(Identifier sound_id) -> {
                loaded_world.players().forEach(player -> 
                    loaded_world.playSound(player, player.blockPosition(), SoundEvent.createFixedRangeEvent(sound_id, 20), SoundSource.PLAYERS, 1, 1)
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
