package com.github.zabbum.oelremakeserver.operations;

import com.github.zabbum.oelremakecomponents.plants.industries.Pumps.PumpsIndustry;

import java.util.ArrayList;
import java.util.List;

public class PumpsIndustryOperations {

    /**
     * Initialize drills industries.
     * @return List with initialized PumpsIndustries
     */
    public static List<PumpsIndustry> initialize() {
        List<PumpsIndustry> pumpProds = new ArrayList<>();

        // Pump productions initialization
        pumpProds.add(new PumpsIndustry("ZASSANICKI GMBH"));
        pumpProds.add(new PumpsIndustry("DR PUMPENER"));

        return pumpProds;
    }
}
