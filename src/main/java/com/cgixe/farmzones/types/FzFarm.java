package com.cgixe.farmzones.types;

import com.cgixe.farmzones.utils.Message;

import javax.annotation.Nullable;
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

    public FzZone createZone(FzZone zone) {
        zones.add(zone);
        return zone;
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(("&dFarm \"" + name + "\""));
        for (FzZone z : zones) {
            str.append('\n').append(z.toString());
        }
        return str.toString();
    }

    public String getName() {
        return name;
    }

    public List<FzZone> getZones() {
        return zones;
    }
    @Nullable
    public FzZone getZone(String name) {
        for (FzZone z : zones) {
            if (z.getName().equals(name)) {
                return z;
            }
        }
        return null;
    }
}
