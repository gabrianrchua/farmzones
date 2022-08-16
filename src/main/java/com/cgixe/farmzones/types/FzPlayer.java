package com.cgixe.farmzones.types;

import com.cgixe.farmzones.utils.Message;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FzPlayer {
    private final String name;
    private final List<FzFarm> farms;

    public FzPlayer(String name) {
        this.name = name;
        farms = new ArrayList<>(0);
    }

    public FzPlayer(String name, List<FzFarm> farms) {
        this.name = name;
        this.farms = farms;
    }

    public FzFarm addFarm(FzFarm farm) {
        farms.add(farm);
        return farm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FzPlayer fzPlayer = (FzPlayer) o;
        return name.equals(fzPlayer.name) && farms.equals(fzPlayer.farms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, farms);
    }

    public String getName() {
        return name;
    }

    public List<FzFarm> getFarms() {
        return farms;
    }

    public String getFarmListString() {
        if (farms.size() == 0) {
            return Message.ColorizeMessage("&9You don't have any farms yet!");
        }
        StringBuilder str = new StringBuilder("&9");
        str.append(farms.get(0).getName());
        for (int i = 1; i < farms.size(); i++) {
            str.append("\n&9").append(farms.get(i).getName());
        }
        return Message.ColorizeMessage(str.toString());
    }
    public List<String> getFarmList() {
        List<String> farmList = new ArrayList<>();
        for (FzFarm farm : farms) {
            farmList.add(farm.getName());
        }
        return farmList;
    }

    @Nullable
    public FzFarm getFarm(String name) {
        for (FzFarm f : farms) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }
}
