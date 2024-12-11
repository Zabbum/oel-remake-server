package com.github.zabbum.oelremakeserver.operations;

import com.github.zabbum.oelrlib.plants.industries.CarsIndustry;

import java.util.ArrayList;
import java.util.List;

public class CarsIndustryOperations {
    /**
     * Initialize cars industries.
     * @return List with initialized CarsIndustries
     */
    public static List<CarsIndustry> initialize() {
        List<CarsIndustry> carsIndustries = new ArrayList<>();

        // Cars productions initialization
        carsIndustries.add(new CarsIndustry("WOZ-PRZEWOZ", 0));
        carsIndustries.add(new CarsIndustry("WAGONENSITZ", 1));
        carsIndustries.add(new CarsIndustry("WORLD CO.", 2));
        carsIndustries.add(new CarsIndustry("DRINK TANK INC.", 3));

        return carsIndustries;
    }
}
