package com.cgixe.farmzones;

import com.cgixe.farmzones.commands.FarmzonesCommands;
import com.cgixe.farmzones.commands.FarmzonesTabcomplete;
import com.cgixe.farmzones.db.ManagerLoader;
import com.cgixe.farmzones.db.ManagerSaver;
import com.cgixe.farmzones.types.FzManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Farmzones extends JavaPlugin {
    public static FzManager manager;

    @Override
    public void onEnable() {
        super.onEnable();

        // initialize fzmanager
        manager = ManagerLoader.LoadManager();

        // register commands
        this.getCommand("farmzones").setExecutor(new FarmzonesCommands());
        this.getCommand("farmzones").setTabCompleter(new FarmzonesTabcomplete());

        Bukkit.getLogger().info("[FarmZones] Enabled FarmZones.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (!ManagerSaver.SaveManager()) {
            Bukkit.getLogger().warning("[FarmZones] Could not save farms to disk. Player farms were not saved!");
        }
        Bukkit.getLogger().info("[FarmZones] Disabled FarmZones.");
    }
}
