package com.cgixe.farmzones.handlers.runnables;

import com.cgixe.farmzones.types.FzLocation;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Callable class that bonemeals a region and takes bonemeal from a player's inventory; returns the number of failed bonemeals
 */
public class BonemealFarmCallable implements Callable<Integer> {
    private final FzLocation pos1;
    private final FzLocation pos2;
    private final World world;
    private final PlayerInventory inventory;

    /**
     * @param pos1 First position
     * @param pos2 Second position
     * @param world The world where the positions are
     * @param inventory The player's inventory
     */
    public BonemealFarmCallable(FzLocation pos1, FzLocation pos2, World world, PlayerInventory inventory) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = world;
        this.inventory = inventory;
    }

    @Override
    public Integer call() {
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

        int totalBlocks = FzLocation.numBlocks(pos1, pos2);
        int successfulBlocks = 0;
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material blockMaterial = block.getType();
                    switch (blockMaterial) {
                        case WHEAT, CARROTS, POTATOES, BEETROOTS, NETHER_WART -> {
                            Ageable ageable = (Ageable) block.getBlockData();
                            if (ageable.getAge() == ageable.getMaximumAge()) {
                                // skip, already fully grown
                                break;
                            }
                            HashMap<Integer, ItemStack> missingItems = inventory.removeItem(new ItemStack(Material.BONE_MEAL, 1));
                            if (!missingItems.isEmpty()) {
                                // skip the rest of the blocks; player doesn't have any more bonemeal
                                return totalBlocks - successfulBlocks;
                            }
                            ageable.setAge(ageable.getMaximumAge());
                            block.setBlockData(ageable);
                            successfulBlocks++;
                        }
                    }
                }
            }
        }
        return 0; // if we got here, the player had enough bonemeal
    }
}
