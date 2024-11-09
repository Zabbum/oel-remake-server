package com.github.zabbum.oelremakeserver.operations;

import com.github.zabbum.oelremakecomponents.plants.industries.CarsIndustry;

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
        carsIndustries.add(new CarsIndustry("WOZ-PRZEWOZ"));
        carsIndustries.add(new CarsIndustry("WAGONENSITZ"));
        carsIndustries.add(new CarsIndustry("WORLD CO."));
        carsIndustries.add(new CarsIndustry("DRINK TANK INC."));

        return carsIndustries;
    }
}
