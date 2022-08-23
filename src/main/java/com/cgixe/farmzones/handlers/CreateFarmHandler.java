package com.cgixe.farmzones.handlers;

import com.cgixe.farmzones.Farmzones;
import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzPlayer;

import javax.annotation.Nullable;

import static com.cgixe.farmzones.Farmzones.manager;

public class CreateFarmHandler {
    public static CreateFarmResult createFarm(String farmName, String playerName) {
        FzPlayer player = manager.getPlayer(playerName);
        if (player.getFarms().size() >= Farmzones.config.getInt("max-num-farms")) {
            return CreateFarmResult.ERROR_MAX_FARMS;
        }
        FzFarm farm = player.getFarm(farmName);
        if (farm != null) {
            return CreateFarmResult.ERROR_FARM_EXISTS;
        }
        player.addFarm(new FzFarm(farmName));
        return CreateFarmResult.SUCCESS;
    }

    public enum CreateFarmResult {
        SUCCESS,
        ERROR_FARM_EXISTS,
        ERROR_MAX_FARMS
    }
}
