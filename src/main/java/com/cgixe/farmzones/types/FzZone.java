package com.cgixe.farmzones.types;

import com.cgixe.farmzones.Farmzones;
import com.cgixe.farmzones.utils.Message;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

/***
 * Defines a FarmZones zone.
 */
public class FzZone implements Serializable {
    private final CropType cropType;
    private final String name;
    @Nullable
    private FzLocation pos1, pos2;

    public FzZone(CropType cropType, String name) {
        this.cropType = cropType;
        this.name = name;
        pos1 = pos2 = null;
    }

    public boolean isComplete() {
        return pos1 != null && pos2 != null;
    }

    /**
     * Adds either zone 1 or 2 to an existing zone
     * @param location The location to add
     * @param isPos1 True if setting the first position, otherwise false for second position
     * @return Returns true if zone was created, otherwise false if the zone is too large as defined by config.yml
     */
    public boolean addZonePos(FzLocation location, boolean isPos1) {
        if (isPos1) {
            pos1 = location;
        } else {
            pos2 = location;
        }

        // validate size if now complete
        if (pos1 != null && pos2 != null) {
            if (FzLocation.numBlocks(pos1, pos2) > Farmzones.config.getInt("max-zone-size")) {
                // unset invalid location
                if (isPos1) {
                    pos1 = null;
                } else {
                    pos2 = null;
                }
                return false;
            }
        }
        return true;
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
        String pos1str = pos1 == null ? "&c[Not assigned]" : "&7" + pos1;
        String pos2str = pos2 == null ? "&c[Not assigned]" : "&7" + pos2;
        return Message.ColorizeMessage("  &9Zone \"" + name + "\"\n    &7" + cropType.toString().toLowerCase() + "\n    " + pos1str + " to " + pos2str);
    }

    // getters
    public CropType getCropType() {
        return cropType;
    }

    @Nullable
    public FzLocation getPos1() {
        return pos1;
    }

    @Nullable
    public FzLocation getPos2() {
        return pos2;
    }

    public String getName() {
        return name;
    }
}
