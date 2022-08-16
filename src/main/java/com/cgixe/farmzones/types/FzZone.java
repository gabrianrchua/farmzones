package com.cgixe.farmzones.types;

import java.util.Objects;

/***
 * Defines a FarmZones zone.
 */
public class FzZone {
    private final CropType cropType;
    private final String name;
    private FzLocation pos1, pos2;

    public FzZone(CropType cropType, String name) {
        this.cropType = cropType;
        this.name = name;
        pos1 = pos2 = null;
    }

    public boolean isComplete() {
        return pos1 != null && pos2 != null;
    }

    public void addZonePos(FzLocation location, boolean isPos1) {
        if (isPos1) {
            pos1 = location;
        } else {
            pos2 = location;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FzZone fzZone = (FzZone) o;
        return cropType == fzZone.cropType && name.equals(fzZone.name) && Objects.equals(pos1, fzZone.pos1) && Objects.equals(pos2, fzZone.pos2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cropType, name, pos1, pos2);
    }

    // getters
    public CropType getCropType() {
        return cropType;
    }

    public FzLocation getPos1() {
        return pos1;
    }

    public FzLocation getPos2() {
        return pos2;
    }

    public String getName() {
        return name;
    }
}
