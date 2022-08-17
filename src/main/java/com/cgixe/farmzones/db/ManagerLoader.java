package com.cgixe.farmzones.db;

import com.cgixe.farmzones.types.FzManager;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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

        FzManager loadedManager = null;
        try {
            FileInputStream fileIn = new FileInputStream("plugins/FarmZones/farms.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            loadedManager = (FzManager) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            Bukkit.getLogger().warning("[FarmZones] IOException while trying to load farms.ser: " + i);
            return new FzManager();
        } catch (ClassNotFoundException c) {
            Bukkit.getLogger().warning("[FarmZones] FzManager class wasn't found when reading farms.ser: " + c);
            return new FzManager();
        }

        return loadedManager;
    }
}
