package com.cgixe.farmzones;

import com.cgixe.farmzones.commands.FarmzonesCommands;
import com.cgixe.farmzones.commands.FarmzonesTabcomplete;
import com.cgixe.farmzones.db.ManagerLoader;
import com.cgixe.farmzones.db.ManagerSaver;
import com.cgixe.farmzones.db.ManagerSaverRunnable;
import com.cgixe.farmzones.types.CropType;
import com.cgixe.farmzones.types.FzManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Farmzones extends JavaPlugin {
    public static FzManager manager;
    public static FileConfiguration config;

    public static EnumMap<CropType, Material> cropMapping = new EnumMap<>(CropType.class);
    public static EnumMap<CropType, Material> seedMapping = new EnumMap<>(CropType.class);


    @Override
    public void onEnable() {
        super.onEnable();

        // initialize fzmanager and material mappings
        manager = ManagerLoader.LoadManager();
        initializeMappings();

        // register commands
        this.getCommand("farmzones").setExecutor(new FarmzonesCommands());
        this.getCommand("farmzones").setTabCompleter(new FarmzonesTabcomplete());

        // load config
        saveDefaultConfig();
        config = getConfig();
        validateConfig();

        // schedule saving
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        int interval = config.getInt("farm-autosave-interval");
        scheduler.scheduleAtFixedRate(new ManagerSaverRunnable(), interval, interval, TimeUnit.SECONDS);

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

    private void validateConfig() {
        InputStream is = getResource("config.yml"); // load OG config
        FileConfiguration cleanConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));

        Set<String> pluginKeys = cleanConfig.getKeys(true);
        Set<String> configKeys = config.getKeys(true);

        for (String s : pluginKeys) {
            if (!configKeys.contains(s)) {
                Bukkit.getLogger().warning("[FarmZones] You are using an invalid FarmZones config. Creating a new one");
                if (!new File("plugins/FarmZones/config.yml").delete()) {
                    Bukkit.getLogger().warning("[FarmZones] Unable to delete invalid FarmZones config. Config will not be saved!");
                }
                this.saveDefaultConfig();

                return;
            }
        }
    }

    private void initializeMappings() {
        cropMapping.put(CropType.WHEAT, Material.WHEAT);
        cropMapping.put(CropType.CARROT, Material.CARROTS);
        cropMapping.put(CropType.POTATO, Material.POTATOES);
        cropMapping.put(CropType.BEETROOT, Material.BEETROOTS);
        cropMapping.put(CropType.NETHERWART, Material.NETHER_WART);

        seedMapping.put(CropType.WHEAT, Material.WHEAT_SEEDS);
        seedMapping.put(CropType.CARROT, Material.CARROT);
        seedMapping.put(CropType.POTATO, Material.POTATO);
        seedMapping.put(CropType.BEETROOT, Material.BEETROOT_SEEDS);
        seedMapping.put(CropType.NETHERWART, Material.NETHER_WART);
    }
}
