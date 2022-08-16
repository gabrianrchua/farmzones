package com.cgixe.farmzones;

import com.cgixe.farmzones.commands.FarmzonesCommands;
import com.cgixe.farmzones.commands.FarmzonesTabcomplete;
import com.cgixe.farmzones.types.FzManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Farmzones extends JavaPlugin {
    public static FzManager manager;

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getLogger().info("[FarmZones] Enabled FarmZones.");

        // initialize fzmanager
        manager = new FzManager();

        // register commands
        this.getCommand("farmzones").setExecutor(new FarmzonesCommands());
        this.getCommand("farmzones").setTabCompleter(new FarmzonesTabcomplete());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getLogger().info("[FarmZones] Disabled FarmZones.");
    }
}
