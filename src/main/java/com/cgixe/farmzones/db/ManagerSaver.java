package com.cgixe.farmzones.db;

import org.bukkit.Bukkit;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
        try {
            FileOutputStream fileOut = new FileOutputStream("plugins/FarmZones/farms.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(manager);
            out.close();
            fileOut.close();
            Bukkit.getLogger().info("[FarmZones] Successfully saved farms to disk.");
            return true;
        } catch (IOException i) {
            Bukkit.getLogger().warning("[FarmZones] IOException while trying to save farms to disk: " + i);
        }
        return false;
    }
}
