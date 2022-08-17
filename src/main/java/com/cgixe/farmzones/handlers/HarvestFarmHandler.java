package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.HashMap;

import static com.cgixe.farmzones.Farmzones.manager;

public class HarvestFarmHandler {
    private static final Material[] validCrops = {
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.NETHER_WART
    };
    /***
     * Harvests all mature crops in a player's farm
     * @param player The player who owns the farm
     * @param farmName The name of the farm to harvest
     * @return Returns true if the operation was successful.
     */
    public static boolean HarvestFarm(Player player, String farmName) {
        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
        if (farm == null) return false;

        World world = player.getWorld();
        PlayerInventory inventory = player.getInventory();
        for (FzZone zone : farm.getZones()) {
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
            /*Bukkit.getLogger().info("zone: " + zone);
            Bukkit.getLogger().info("x: " + startX + " to " + endX);
            Bukkit.getLogger().info("y: " + startY + " to " + endY);
            Bukkit.getLogger().info("z: " + startZ + " to " + endZ);*/
            for (int y = startY; y <= endY; y++) {
                for (int x = startX; x <= endX; x++) {
                    for (int z = startZ; z <= endZ; z++) {
                        Block block = world.getBlockAt(x, y, z);
                        Material blockMaterial = block.getType();
                        //Bukkit.getLogger().info("block " + x + "," + y + "," + z + ", mat=" + blockMaterial);
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
        return true;
    }
}
