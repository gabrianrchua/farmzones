package com.cgixe.farmzones.handlers;

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

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;

import static com.cgixe.farmzones.Farmzones.manager;

public class ReplantFarmHandler {
    /***
     * Replants a player's farm with the user-set crops
     * @param player The player who owns the farm
     * @param farmName The name of the farm
     * @return Returns an int representing the number of failed plantings (not enough items, not correct soil underneath, crop space wasn't empty). -999 = farm doesn't exist.
     */
    public static int ReplantFarm(Player player, String farmName) {
        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
        if (farm == null) return -999;

        EnumMap<CropType, Material> cropMapping = new EnumMap<>(CropType.class);
        cropMapping.put(CropType.WHEAT, Material.WHEAT);
        cropMapping.put(CropType.CARROT, Material.CARROTS);
        cropMapping.put(CropType.POTATO, Material.POTATOES);
        cropMapping.put(CropType.BEETROOT, Material.BEETROOTS);
        cropMapping.put(CropType.NETHERWART, Material.NETHER_WART);

        EnumMap<CropType, Material> seedMapping = new EnumMap<>(CropType.class);
        seedMapping.put(CropType.WHEAT, Material.WHEAT_SEEDS);
        seedMapping.put(CropType.CARROT, Material.CARROT);
        seedMapping.put(CropType.POTATO, Material.POTATO);
        seedMapping.put(CropType.BEETROOT, Material.BEETROOT_SEEDS);
        seedMapping.put(CropType.NETHERWART, Material.NETHER_WART);

        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();
        int failedPlantCounter = 0;
        for (FzZone zone : farm.getZones()) {
            CropType fzCropType = zone.getCropType();
            Material cropMaterial = cropMapping.get(fzCropType);
            Material seed = seedMapping.get(fzCropType);

            FzLocation pos1 = zone.getPos1();
            FzLocation pos2 = zone.getPos2();
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
