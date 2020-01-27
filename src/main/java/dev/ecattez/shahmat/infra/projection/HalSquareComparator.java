package dev.ecattez.shahmat.infra.projection;

import java.util.Comparator;

public class HalSquareComparator implements Comparator<HalSquare> {

    @Override
    public int compare(HalSquare s1, HalSquare s2) {
        return s1.getLocation().compareTo(s2.getLocation());
    }

}
