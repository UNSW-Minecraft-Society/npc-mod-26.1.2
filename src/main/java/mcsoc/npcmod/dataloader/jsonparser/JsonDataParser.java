package mcsoc.npcmod.dataloader.jsonparser;

import com.google.gson.Gson;

import mcsoc.npcmod.NpcMod;
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


public interface JsonDataParser {

    abstract Gson getGsonParser();

    private File openFileChecked(String filename) {
        Path config_path = FabricLoader.getInstance().getConfigDir().resolve(filename);
        try {
            Files.createDirectories(config_path.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return config_path.toFile();
    }

    default <K, V> void writeToFileFromMap(String filename, Map<K, V> map, Type data_type) {
        File file = openFileChecked(filename);
        
        try (FileWriter writer = new FileWriter(file)) {
            this.getGsonParser().toJson(map, data_type, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    default <K, V> Map<K, V> readFromFileToMap(String filename, Type data_type) {
        File file = openFileChecked(filename);

        if (file == null || !file.exists() || file.isDirectory()) {
            NpcMod.LOGGER.error("[" + filename + "] File does not exist. Skipping read.");
            return new HashMap<>(); 
        }
        
        Map<K, V> map = new HashMap<>();
        try (FileReader reader = new FileReader(file)) {
            map = this.getGsonParser().fromJson(reader, data_type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map != null ? map : new HashMap<>();
    }
}