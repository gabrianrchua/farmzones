package com.cgixe.farmzones.zones;

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

    public FzFarm getFarm(String name) {
        for (FzFarm f : farms) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }
}
