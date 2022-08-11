package com.cgixe.farmzones;

import com.cgixe.farmzones.commands.FarmzonesCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Farmzones extends JavaPlugin {
    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getLogger().info("Hello world!!!!!!!!!!!!!!!!!!!");

        // register commands
        this.getCommand("farmzones").setExecutor(new FarmzonesCommands());
    }
}
