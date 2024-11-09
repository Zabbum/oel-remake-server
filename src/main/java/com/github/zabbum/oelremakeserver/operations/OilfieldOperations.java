package com.github.zabbum.oelremakeserver.operations;

import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;

import java.util.ArrayList;
import java.util.List;

public class OilfieldOperations {

    /**
     * Initialize oilfields.
     * @return List with initialized Oilfields
     */
    public static List<Oilfield> initialize() {
        List<Oilfield> oilfields = new ArrayList<>();

        // Oilfields initialization
        oilfields.add(new Oilfield("JASNY GWINT", 0));
        oilfields.add(new Oilfield("WIELKA DZIURA", 1));
        oilfields.add(new Oilfield("WIERTOWISKO", 2));
        oilfields.add(new Oilfield("SMAK WALUTY", 3));
        oilfields.add(new Oilfield("MI£A ZIEMIA", 4));
        oilfields.add(new Oilfield("BORUJ-BORUJ", 5));
        oilfields.add(new Oilfield("KRASNY POTOK", 6));
        oilfields.add(new Oilfield("P£YTKIE DO£Y", 7));
        oilfields.add(new Oilfield("$LADY OLEJU", 8));
        oilfields.add(new Oilfield("NICZYJ GRUNT", 9));
        oilfields.add(new Oilfield("DZIKIE PSY", 10));
        oilfields.add(new Oilfield("UGORY NAFTOWE", 11));

        return oilfields;
    }
}
