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
        oilfields.add(new Oilfield("JASNY GWINT"));
        oilfields.add(new Oilfield("WIELKA DZIURA"));
        oilfields.add(new Oilfield("WIERTOWISKO"));
        oilfields.add(new Oilfield("SMAK WALUTY"));
        oilfields.add(new Oilfield("MI£A ZIEMIA"));
        oilfields.add(new Oilfield("BORUJ-BORUJ"));
        oilfields.add(new Oilfield("KRASNY POTOK"));
        oilfields.add(new Oilfield("P£YTKIE DO£Y"));
        oilfields.add(new Oilfield("$LADY OLEJU"));
        oilfields.add(new Oilfield("NICZYJ GRUNT"));
        oilfields.add(new Oilfield("DZIKIE PSY"));
        oilfields.add(new Oilfield("UGORY NAFTOWE"));

        return oilfields;
    }
}
