package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.handlers.runnables.HarvestFarmRunnable;
import com.cgixe.farmzones.types.FzFarm;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

import static com.cgixe.farmzones.Farmzones.manager;

public class HarvestFarmHandler {
    public static final Material[] validCrops = {
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.NETHER_WART
    };
    /**
     * Harvests all mature crops in a player's farm
     * @param player The player who owns the farm
     * @param farmName The name of the farm to harvest
     * @return Returns true if the operation was successful (the farm exists).
     */
    public static boolean HarvestFarm(Player player, String farmName) {
        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
        if (farm == null) return false;

        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();
        new HarvestFarmRunnable(farm, world, inventory, player).run();
        return true;
    }

    /**
     * Harvests all a player's farms
     * @param player The player to harvest from
     * @return Returns the number of farms that were harvested successfully
     */
    public static int HarvestAllFarms(Player player) {
        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();

        List<FzFarm> farms = manager.getPlayer(player.getName()).getFarms();
        for (FzFarm farm : farms) {
            new HarvestFarmRunnable(farm, world, inventory, player).run();
        }
        return farms.size(); // not possible that a farm doesn't exist; just return the size()
    }
}
