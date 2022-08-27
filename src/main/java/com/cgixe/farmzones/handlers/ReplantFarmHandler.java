package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.handlers.runnables.ReplantFarmCallable;
import com.cgixe.farmzones.types.FzFarm;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

import static com.cgixe.farmzones.Farmzones.manager;

public class ReplantFarmHandler {
    /**
     * Replants a player's farm with the user-set crops
     * @param player The player who owns the farm
     * @param farmName The name of the farm
     * @return Returns an int representing the number of failed plantings (not enough items, not correct soil underneath, crop space wasn't empty). -999 = farm doesn't exist.
     */
    public static int ReplantFarm(Player player, String farmName) {
        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
        if (farm == null) return -999;

        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();
        return new ReplantFarmCallable(farm, world, inventory).call();
    }

    /**
     * Replants all of a player's farms with the user-set crops
     * @param player The player who owns the farms
     * @return Returns an int representing the number of failed plantings (not enough items, not correct soil underneath, crop space wasn't empty)
     */
    public static int ReplantAllFarms(Player player) {
        List<FzFarm> farms = manager.getPlayer(player.getName()).getFarms();
        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();

        int totalFailedPlantings = 0;
        for (FzFarm farm : farms) {
            totalFailedPlantings += new ReplantFarmCallable(farm, world, inventory).call();
        }
        return totalFailedPlantings;
    }
}
