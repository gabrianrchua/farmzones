package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.types.*;

import static com.cgixe.farmzones.Farmzones.config;
import static com.cgixe.farmzones.Farmzones.manager;

public class AddZoneHandler {
    public static AddZoneResult addZone(String playerName, String farmName, String zoneName, CropType type, boolean isPos1, FzLocation location) {
        FzFarm farm = manager.getPlayer(playerName).getFarm(farmName);
        if (farm == null) {
            return AddZoneResult.ERROR_FARM_NOT_EXIST;
        }
        if (farm.getZones().size() >= config.getInt("max-num-zones")) {
            return AddZoneResult.ERROR_MAX_NUM_ZONES;
        }
        FzZone zone = farm.getZone(zoneName);
        if (zone != null) {
            // add to existing zone
            if (zone.addZonePos(location, isPos1)) {
                return AddZoneResult.SUCCESS;
            }
        } else {
            // create new zone
            FzZone newZone = farm.createZone(new FzZone(type, zoneName));
            if (newZone.addZonePos(location, isPos1)) {
                return AddZoneResult.SUCCESS;
            }
        }
        return AddZoneResult.ERROR_MAX_ZONE_SIZE;
    }

    public enum AddZoneResult {
        SUCCESS,
        ERROR_FARM_NOT_EXIST,
        ERROR_MAX_NUM_ZONES,
        ERROR_MAX_ZONE_SIZE
    }
}
