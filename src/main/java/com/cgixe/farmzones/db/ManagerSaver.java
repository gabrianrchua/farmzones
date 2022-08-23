package com.cgixe.farmzones.db;

import org.bukkit.Bukkit;

import java.io.*;

import static com.cgixe.farmzones.Farmzones.manager;

/***
 * Responsible for saving FzManager to plugins/FarmZones/farms.ser
 */
public class ManagerSaver {
    /***
     * Saves FzManager to disk
     * @return Returns true if the operation was successful
     */
    public static boolean SaveManager() {
        File file = new File("plugins/FarmZones/farms.json");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    Bukkit.getLogger().warning("[FarmZones] Unable to create farms.json file.");
                }
            } catch (IOException e) {
                Bukkit.getLogger().warning("[FarmZones] IOException while trying to create farms.json file: " + e);
            }
        }
        try {
            FileOutputStream fileOut = new FileOutputStream("plugins/FarmZones/farms.json");
            String json = manager.toJson();
            fileOut.write(json.getBytes());
            fileOut.close();
            Bukkit.getLogger().info("[FarmZones] Saved farms.json.");
            return true;
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().warning("[FarmZones] Unable to save farms.json file: not found! Was it deleted?");
            return false;
        } catch (IOException e) {
            Bukkit.getLogger().warning("[FarmZones] IOException while trying to save farms.json file: " + e);
            return false;
        }
    }
}
