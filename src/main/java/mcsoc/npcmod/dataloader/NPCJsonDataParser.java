package mcsoc.npcmod.dataloader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mcsoc.npcmod.dataloader.NPCServerDataLoader.DialogueData;
import mcsoc.npcmod.dataloader.NPCServerDataLoader.ModelData;
import mcsoc.npcmod.dataloader.NPCServerDataLoader.NPCData;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NPCJsonDataParser {
    // With default settings can use
    private static final Gson mapper = new GsonBuilder()
            .registerTypeAdapter(DialogueData.class, new DialogueData.JsonSerialiser())
            .registerTypeAdapter(ModelData.class, new ModelData.JsonSerialiser())
            .registerTypeAdapter(NPCData.class, new NPCData.JsonSerialiser())
            .setPrettyPrinting()
            .create();
    private static final Type model_map_type = new TypeToken<Map<String, ModelData>>(){}.getType();
    private static final Type dialogue_map_type = new TypeToken<Map<String, DialogueData>>(){}.getType();
    private static final Type npc_map_type = new TypeToken<Map<String, NPCData>>(){}.getType();

    private static final NPCJsonDataParser INSTANCE = new NPCJsonDataParser(); 

    private NPCJsonDataParser() {}


    public static NPCJsonDataParser getInstance() {
        return INSTANCE;
    }


    private File openFileChecked(String filename) {
        Path config_path = FabricLoader.getInstance().getConfigDir().resolve(filename);
        try {
            Files.createDirectories(config_path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return config_path.toFile();
    }


    public void saveModelData(String filename, Map<String, ModelData> model_data_map) {
        
        File file = openFileChecked(filename);
        
        try (FileWriter writer = new FileWriter(file)) {
            mapper.toJson(model_data_map, model_map_type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, ModelData> loadModelData(String filename) {
        
        File file = openFileChecked(filename);
        
        Map<String, ModelData> model_dataset = new HashMap<>();
        try (FileReader reader = new FileReader(file)) {
            model_dataset = mapper.fromJson(reader, model_map_type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return model_dataset;
    }

    public void saveDialogueData(String filename, Map<String, DialogueData> dialogue_map) {
        
        File file = openFileChecked(filename);
        
        try (FileWriter writer = new FileWriter(file)) {
            mapper.toJson(dialogue_map, dialogue_map_type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, DialogueData> loadDialogueData(String filename) {
        
        File file = openFileChecked(filename);
        
        Map<String, DialogueData> dialogue_data = new HashMap<>();
        try (FileReader reader = new FileReader(file)) {
            dialogue_data = mapper.fromJson(reader, dialogue_map_type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dialogue_data;
    }

    public void saveNPCData(String filename, Map<String, NPCData> npc_data_map) {
        
        File file = openFileChecked(filename);
        
        try (FileWriter writer = new FileWriter(file)) {
            mapper.toJson(npc_data_map, npc_map_type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Map<String, NPCData> loadNPCData(String filename) {
        
        File file = openFileChecked(filename);
        
        Map<String, NPCData> npc_dataset = null;
        try (FileReader reader = new FileReader(file)) {
            npc_dataset = mapper.fromJson(reader, npc_map_type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Objects.isNull(npc_dataset) ? HashMap.newHashMap(0) : npc_dataset;
    }
}