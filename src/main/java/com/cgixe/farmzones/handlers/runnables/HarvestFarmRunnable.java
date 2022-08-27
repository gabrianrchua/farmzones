package com.cgixe.farmzones.handlers.runnables;

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
import java.util.HashMap;

import static com.cgixe.farmzones.handlers.HarvestFarmHandler.validCrops;

/**
 * Runnable class that harvests a farm and gives the results to the player
 */
public class HarvestFarmRunnable implements Runnable {
    private final FzFarm farm;
    private final World world;
    private final PlayerInventory inventory;
    private final Player player;

    public HarvestFarmRunnable(FzFarm farm, World world, PlayerInventory inventory, Player player) {
        this.farm = farm;
        this.world = world;
        this.inventory = inventory;
        this.player = player;
    }

    @Override
    public void run() {
        for (FzZone zone : farm.getZones()) {
            FzLocation pos1 = zone.getPos1();
            FzLocation pos2 = zone.getPos2();
            if (pos1 == null || pos2 == null) {
                // skip the zone, it is not complete
                break;
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
                        for (Material valid : validCrops) {
                            if (valid == blockMaterial) {
                                Collection<ItemStack> drops = block.getDrops();
                                HashMap<Integer, ItemStack> surplusItems = inventory.addItem(drops.toArray(new ItemStack[0]));
                                if (!surplusItems.isEmpty()) {
                                    // drop surplus items on the ground
                                    for (ItemStack stack : surplusItems.values()) {
                                        world.dropItem(player.getLocation(), stack);
                                    }
                                }
                                block.setType(Material.AIR);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
