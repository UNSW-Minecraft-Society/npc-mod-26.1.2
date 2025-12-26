package mcsoc.planetgame.entities.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mcsoc.planetgame.PlanetGame;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import oshi.hardware.SoundCard;

public class NPCServerDataLoader {
    public static record NPCData(String model_id, String dialogue_id, Mode mode) {}
    public static record ModelData(UUID uuid) {}
    public static record DialogueData(Text display_name, Text dialogue, Identifier voice) {
        public Text getFormattedMessage() {
            return display_name.copy().append(": ").append(dialogue);
        }
    }
    public enum Mode {
        AMBIENT,
        STORY
    }

    public static final NPCServerDataLoader INSTANCE = new NPCServerDataLoader();

    public static final ModelData MODEL_NOT_FOUND = new ModelData(UUID.fromString("e2d03233-e72a-4389-9b41-12a9ff98bf3d"));
    public static final DialogueData DIALOGUE_NOT_FOUND = new DialogueData(Text.of("DEFAULT"),Text.of("Dialogue not found."), Identifier.ofVanilla("music_disc.far"));
    public static final String MISSING_KEY = "mssngn";
    public static final NPCData NPC_NOT_FOUND = new NPCData(MISSING_KEY, MISSING_KEY, Mode.AMBIENT);

    public final Map<String, NPCData> npc_data_map = new HashMap<>();
    public final Map<String, ModelData> model_data_map = new HashMap<>();
    public final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    public static NPCServerDataLoader getInstance() {
        return INSTANCE;
    }

    private NPCServerDataLoader() {

        model_data_map.put(
            "john",
            new ModelData(UUID.fromString("d158fd5e-4367-484c-a338-a1cb950d6355"))
        );
        dialogue_data_map.put(
            "john",
            new DialogueData(
                Text.of("John Planet"),
                Text.of("Hey! I'm John Planet! Welcome to my planet!"),
                Identifier.ofVanilla("entity.villager.celebrate")
            )
        );
        npc_data_map.put("john planet", new NPCData("john", "john", Mode.STORY));

        model_data_map.put(
            "gw",
            new ModelData(UUID.fromString("79cdda80-d9aa-458d-bd80-1ea92df06d35"))
        );
        dialogue_data_map.put(
            "gw",
            new DialogueData(
                Text.of("Mr Game & Watch"),
                Text.of("Hi! It's me, Mr. Game & Watch from Smash!"),
                Identifier.ofVanilla("block.anvil.use")
            )
        );
        npc_data_map.put("game and watch", new NPCData("gw", "gw", Mode.AMBIENT));
    }

    public static void loadData() {
        //
    }

    public DialogueData getDialogue(BasicNPC npc) {
        NPCData npc_data = npc_data_map.getOrDefault(npc.getID(), NPC_NOT_FOUND);
        return dialogue_data_map.getOrDefault(npc_data.dialogue_id(), DIALOGUE_NOT_FOUND);   
    }
    public ModelData getModel(BasicNPC npc) {
        NPCData npc_data = npc_data_map.getOrDefault(npc.getID(), NPC_NOT_FOUND);
        return model_data_map.getOrDefault(npc_data.model_id(), MODEL_NOT_FOUND);
    }
}
