package com.cgixe.farmzones.db;

import com.cgixe.farmzones.types.FzManager;
import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.*;

/***
 * Responsible for loading FzManager class from disk if it exists. Otherwise, returns a new instance of FzManager
 */
public class ManagerLoader {
    public static FzManager LoadManager() {
        File pluginFolder = new File("plugins/FarmZones");
        if (!pluginFolder.exists()) {
            Bukkit.getLogger().info("[FarmZones] Plugin folder doesn't exist, creating");
            if (!pluginFolder.mkdir()) {
                // if unsuccessful, return an empty FzManger
                Bukkit.getLogger().warning("[FarmZones] Unable to create plugins/FarmZones folder");
                return new FzManager();
            }
        }

        FzManager loadedManager;
        try {
            FileInputStream fileIn = new FileInputStream("plugins/FarmZones/farms.json");
            String jsonStr = new String(fileIn.readAllBytes());
            Gson json = new Gson();
            loadedManager = json.fromJson(jsonStr, FzManager.class);
            fileIn.close();
            return loadedManager;
        } catch (IOException e) {
            // suppress file not found; just create a new one
            if (!(e instanceof FileNotFoundException)) {
                Bukkit.getLogger().warning("[FarmZones] IOException while trying to load farms.json: " + e);
            }
            return new FzManager();
        }
        /*
        try {
            FileInputStream fileIn = new FileInputStream("plugins/FarmZones/farms.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedManager = (FzManager) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            // Suppress file not found message
            if (!(i instanceof FileNotFoundException)) {
                Bukkit.getLogger().warning("[FarmZones] IOException while trying to load farms.ser: " + i);
            }
            return new FzManager();
        } catch (ClassNotFoundException c) {
            Bukkit.getLogger().warning("[FarmZones] FzManager class wasn't found when reading farms.ser: " + c);
            Bukkit.getLogger().warning("[FarmZones] This usually means the file was corrupted. Creating a new one.");
            return new FzManager();
        }

        return loadedManager;*/
    }
}
