package com.cgixe.farmzones.zones;

import java.util.Objects;

/***
 * Defines a FarmZones zone.
 */
public class FzZone {
    private final CropType cropType;
    private final int pos1;
    private final int pos2;

    public FzZone(CropType cropType, int pos1, int pos2) {
        this.cropType = cropType;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FzZone fzZone = (FzZone) o;
        return pos1 == fzZone.pos1 && pos2 == fzZone.pos2 && cropType == fzZone.cropType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cropType, pos1, pos2);
    }

    // getters
    public CropType getCropType() {
        return cropType;
    }

    public int getPos1() {
        return pos1;
    }

    public int getPos2() {
        return pos2;
    }
}
