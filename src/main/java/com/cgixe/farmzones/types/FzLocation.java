package com.cgixe.farmzones.types;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.Objects;

/***
 * Stores a coordinate in xyz space (integers only).
 */
public class FzLocation implements Serializable {
    private final int x, y, z;
    public FzLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public FzLocation(Location location) {
        x = (int) Math.floor(location.getX());
        y = (int) Math.round(location.getY());
        z = (int) Math.floor(location.getZ());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FzLocation that = (FzLocation) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
