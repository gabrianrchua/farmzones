package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzPlayer;

import javax.annotation.Nullable;

import static com.cgixe.farmzones.Farmzones.manager;

public class CreateFarmHandler {
    @Nullable
    public static FzFarm createFarm(String farmName, String playerName) {
        FzPlayer tmpPlayer = manager.getPlayer(playerName);
        FzPlayer player = tmpPlayer != null ? tmpPlayer : manager.addPlayer(new FzPlayer(playerName));
        FzFarm tmpFarm = player.getFarm(farmName);
        if (tmpFarm != null) {
            return null;
        } else {
            return player.addFarm(new FzFarm(farmName));
        }
    }
}
