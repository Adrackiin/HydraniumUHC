package fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers;

import org.bukkit.Material;

import java.util.Objects;

public class TypeID {

    private final Material material;
    private final short data;

    public TypeID(Material material, short data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }

    public short getData() {
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, data);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        TypeID typeID = (TypeID) o;
        return data == typeID.data && material == typeID.material;
    }
}
