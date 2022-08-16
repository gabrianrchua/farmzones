package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzPlayer;
import com.cgixe.farmzones.types.FzZone;

import static com.cgixe.farmzones.Farmzones.manager;

public class DeleteHandler {
    /***
     * Deletes a zone from a player's farm
     * @param playerName The player's username
     * @param farmName The farm to delete from
     * @param zoneName The zone to delete
     * @return Returns true if the operation was successful
     */
    public static boolean DeleteZone(String playerName, String farmName, String zoneName) {
        FzFarm farm = manager.getPlayer(playerName).getFarm(farmName);
        if (farm != null) {
            FzZone zoneToRemove = farm.getZone(zoneName);
            if (zoneToRemove != null) {
                farm.getZones().remove(zoneToRemove);
                return true;
            }
        }
        return false;
    }

    /***
     * Deletes a player's farm
     * @param playerName The player's username
     * @param farmName The farm to delete
     * @return Returns true if the operation was successful
     */
    public static boolean DeleteFarm(String playerName, String farmName) {
        FzPlayer player = manager.getPlayer(playerName);
        FzFarm farmToDelete = player.getFarm(farmName);
        if (farmToDelete != null) {
            player.getFarms().remove(farmToDelete);
            return true;
        }
        return false;
    }
}
