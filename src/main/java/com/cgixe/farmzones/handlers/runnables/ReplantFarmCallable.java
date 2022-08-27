package com.cgixe.farmzones.handlers.runnables;

import com.cgixe.farmzones.Farmzones;
import com.cgixe.farmzones.types.CropType;
import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Callable class that replants a player's farm and takes the seeds from their inventory; returns the number of crops that failed to be planted
 */
public class ReplantFarmCallable implements Callable<Integer> {
    private final FzFarm farm;
    private final World world;
    private final PlayerInventory inventory;

    public ReplantFarmCallable(FzFarm farm, World world, PlayerInventory inventory) {
        this.farm = farm;
        this.world = world;
        this.inventory = inventory;
    }

    @Override
    public Integer call() {
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
