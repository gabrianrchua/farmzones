package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.handlers.runnables.BonemealFarmCallable;
import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

import static com.cgixe.farmzones.Farmzones.manager;

public class BonemealFarmHandler {
    /**
     * Bonemeals a player's farm, taking 1 bonemeal per crop
     * @param player The player who owns the farm
     * @param farmName The farm to bonemeal
     * @return Returns an int representing the number of crops that couldn't be bonemealed (not enough bonemeal in inventory). -999 = farm doesn't exist
     */
    public static int BonemealFarm(Player player, String farmName) {
        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
        if (farm == null) {
            return -999;
        }
        return bonemealFarm(farm, player.getWorld(), player.getInventory());
    }

    /**
     * Bonemeals all of a player's farms, taking 1 bonemeal per crop
     * @param player The player who owns the farm
     * @return Returns an int representing the number of crops that couldn't be bonemealed (not enough bonemeal in inventory)
     */
    public static int BonemealAllFarms(Player player) {
        List<FzFarm> farms = manager.getPlayer(player.getName()).getFarms();
        int failedBonemeal = 0;
        for (FzFarm farm : farms) {
            failedBonemeal += bonemealFarm(farm, player.getWorld(), player.getInventory());
        }
        return failedBonemeal;
    }

    /**
     * Helper method to bonemeal a player's farm
     * @param farm The FzFarm to bonemeal
     * @param world The world that the farm exists in
     * @param inventory The player's inventory to take the bonemeal from
     * @return Returns the number of blocks that failed to be bonemealed
     */
    private static int bonemealFarm(FzFarm farm, World world, PlayerInventory inventory) {
        int failedBlocks = 0;
        for (FzZone zone : farm.getZones()) {
            FzLocation pos1 = zone.getPos1();
            FzLocation pos2 = zone.getPos2();
            if (pos1 == null || pos2 == null) {
                // skip the zone, it is not complete
                break;
            }

            BonemealFarmCallable callable = new BonemealFarmCallable(pos1, pos2, world, inventory);
            failedBlocks += callable.call();;
        }
        return failedBlocks;
    }
}
