package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.types.*;

import javax.annotation.Nullable;

import static com.cgixe.farmzones.Farmzones.manager;

public class AddZoneHandler {
    @Nullable
    public static FzZone addZone(String playerName, String farmName, String zoneName, CropType type, boolean isPos1, FzLocation location) {
        FzFarm farm = manager.getPlayer(playerName).getFarm(farmName);
        if (farm == null) {
            return null;
        }
        FzZone zone = farm.getZone(zoneName);
        if (zone != null) {
            // add to existing zone
            zone.addZonePos(location, isPos1);
            return zone;
        } else {
            // create new zone
            FzZone newZone = farm.createZone(new FzZone(type, zoneName));
            newZone.addZonePos(location, isPos1);
            return newZone;
        }
    }
}
