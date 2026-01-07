package mcsoc.npcmod.dataloader.datastorage.npc;

import java.util.List;
import java.util.Map;

import mcsoc.npcmod.dataloader.datastorage.datatypes.Mode;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementInstruction;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovingNPCData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;
import mcsoc.npcmod.entities.npc.BaseNPC;
import mcsoc.npcmod.entities.npc.MovingNPC;

public interface MovingNPCDataStorage {

    static final MovementData MOVEMENTS_NOT_FOUND = new MovementData(List.of(new MovementInstruction.Jump()));
    static final MovingNPCData MOVING_NPC_NOT_FOUND = new MovingNPCData(NPCDataStorage.NPC_NOT_FOUND, NPCDataStorage.MISSING_KEY);

    abstract Map<String, MovingNPCData> getMovingNPCMap();
    abstract Map<String, MovementData> getMovementMap();

    abstract NPCData getNPCData(BaseNPC npc);

    default MovingNPCData getMovingNPCData(MovingNPC npc) {
        return this.getMovingNPCMap().getOrDefault(npc.getID(), MOVING_NPC_NOT_FOUND);
    }

    public default MovementData getMovements(MovingNPC npc) {
        MovingNPCData npc_data = this.getMovingNPCData(npc);
        return this.getMovementMap().getOrDefault(npc_data.movement_id(), MOVEMENTS_NOT_FOUND);
    }
    public default void registerMovementData(String id, MovementData movements) {
        this.getMovementMap().put(id, movements);
    }
    public default void registerMovingNPC(String id, String model_id, String dialogue_id, String movement_id) {
        this.getMovingNPCMap().put(id, new MovingNPCData(new NPCData(model_id, dialogue_id, Mode.MOVING), movement_id));
    }
}
