package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.Farmzones;
import com.cgixe.farmzones.types.CropType;
import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
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
        return replantFarm(farm, world, inventory);
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
            totalFailedPlantings += replantFarm(farm, world, inventory);
        }
        return totalFailedPlantings;
    }

    private static int replantFarm(FzFarm farm, World world, PlayerInventory inventory) {
        int failedPlantCounter = 0;
        for (FzZone zone : farm.getZones()) {
            CropType fzCropType = zone.getCropType();
            Material cropMaterial = Farmzones.cropMapping.get(fzCropType);
            Material seed = Farmzones.seedMapping.get(fzCropType);

            FzLocation pos1 = zone.getPos1();
            FzLocation pos2 = zone.getPos2();
            if (pos1 == null || pos2 == null) {
                break; // skip, zone isn't complete
            }
            int startX, endX, startY, endY, startZ, endZ;
            // x coordinate
            if (pos1.getX() <= pos2.getX()) {
                startX = pos1.getX();
                endX = pos2.getX();
            } else {
                startX = pos2.getX();
                endX = pos1.getX();
            }
            // y coordinate
            if (pos1.getY() <= pos2.getY()) {
                startY = pos1.getY();
                endY = pos2.getY();
            } else {
                startY = pos2.getY();
                endY = pos1.getY();
            }
            // z coordinate
            if (pos1.getZ() <= pos2.getZ()) {
                startZ = pos1.getZ();
                endZ = pos2.getZ();
            } else {
                startZ = pos2.getZ();
                endZ = pos1.getZ();
            }

            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                    for (int z = startZ; z <= endZ; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        Material blockMaterial = block.getType();
                        Block below = world.getBlockAt(x, y - 1, z);
                        switch (cropMaterial) {
                            case WHEAT:
                            case CARROTS:
                            case POTATOES:
                            case BEETROOTS:
                                if (below.getType() == Material.FARMLAND && blockMaterial == Material.AIR) {
                                    HashMap<Integer, ItemStack> result = inventory.removeItem(new ItemStack(seed, 1));
                                    if (result.isEmpty()) {
                                        block.setType(cropMaterial);
                                    } else {
                                        failedPlantCounter++;
                                    }
                                } else {
                                    failedPlantCounter++;
                                }
                                break;
                            case NETHER_WART:
                                if (below.getType() == Material.SOUL_SAND) {
                                    HashMap<Integer, ItemStack> result = inventory.removeItem(new ItemStack(seed, 1));
                                    if (result.isEmpty()) {
                                        block.setType(cropMaterial);
                                    } else {
                                        failedPlantCounter++;
                                    }
                                } else {
                                    failedPlantCounter++;
                                }
                                break;
                        }
                    }
                }
            }
        }
        return failedPlantCounter;
    }
}
