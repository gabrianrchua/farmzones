package com.cgixe.farmzones.types;

import com.cgixe.farmzones.utils.Message;

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

    @Override
    public String toString() {
        return Message.ColorizeMessage("  &9Zone \"" + name + "\"\n    &7" + cropType.toString().toLowerCase() + "\n    &7" + pos1.toString() + " to " + pos2.toString());
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
