package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.zones.FzFarm;
import com.cgixe.farmzones.zones.FzPlayer;

import static com.cgixe.farmzones.Farmzones.manager;

public class CreateFarmHandler {
    public static FzFarm createFarm(String farmName, String playerName) {
        FzPlayer tmpPlayer = manager.getPlayer(playerName);
        FzPlayer player = tmpPlayer != null ? tmpPlayer : manager.addPlayer(new FzPlayer(playerName));
        FzFarm tmpFarm = player.getFarm(farmName);
        if (tmpFarm != null) {
            return tmpFarm;
        } else {
            return player.addFarm(new FzFarm(farmName));
        }
    }
}
