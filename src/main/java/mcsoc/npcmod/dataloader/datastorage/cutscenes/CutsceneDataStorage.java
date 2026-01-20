package mcsoc.npcmod.dataloader.datastorage.cutscenes;

import java.util.List;
import java.util.Map;

import mcsoc.npcmod.datatypes.cutscenes.CutsceneData;
import mcsoc.npcmod.datatypes.cutscenes.CutsceneInstruction;
import net.minecraft.text.Text;


public interface CutsceneDataStorage {
    
    static final CutsceneData CUTSCENE_NOT_FOUND = new CutsceneData(List.of(new CutsceneInstruction.Dialogue(Text.of("Hello, world!")), new CutsceneInstruction.Delay(20)));

    abstract Map<String, CutsceneData> getCutsceneMap();

    public default CutsceneData getCutscene(String id) {
        return this.getCutsceneMap().getOrDefault(id, CUTSCENE_NOT_FOUND);
    }
    public default void registerCutsceneData(String id, CutsceneData actions) {
        this.getCutsceneMap().put(id, actions);
    }
}
