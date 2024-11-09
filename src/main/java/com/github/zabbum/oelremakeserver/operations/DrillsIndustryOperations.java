package com.github.zabbum.oelremakeserver.operations;

import com.github.zabbum.oelremakecomponents.plants.industries.DrillsIndustry;

import java.util.ArrayList;
import java.util.List;

public class DrillsIndustryOperations {
    /**
     * Initialize drills industries.
     *
     * @return List with initialized DrillsIndustries
     */
    public static List<DrillsIndustry> initialize() {
        List<DrillsIndustry> drillProds = new ArrayList<>();

        // Drills productions initialization
        drillProds.add(new DrillsIndustry("TURBOWIERT"));
        drillProds.add(new DrillsIndustry("NA B£YSK INC."));
        drillProds.add(new DrillsIndustry("PET SHOP&BOYS"));

        return drillProds;
    }
}
