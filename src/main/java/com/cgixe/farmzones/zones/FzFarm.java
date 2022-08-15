package com.cgixe.farmzones.zones;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FzFarm {
    private final String name;
    private final List<FzZone> zones;

    public FzFarm(String name) {
        this.name = name;
        zones = new ArrayList<>(0);
    }
    public FzFarm(String name, List<FzZone> zones) {
        this.name = name;
        this.zones = zones;
    }

    public void addZone(FzZone zone) {
        zones.add(zone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FzFarm fzFarm = (FzFarm) o;
        return name.equals(fzFarm.name) && zones.equals(fzFarm.zones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, zones);
    }

    public String getName() {
        return name;
    }

    public List<FzZone> getZones() {
        return zones;
    }
}
