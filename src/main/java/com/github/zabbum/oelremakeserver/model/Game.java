package com.github.zabbum.oelremakeserver.model;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.plants.industries.Cars.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Drills.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Pumps.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import lombok.Data;

import java.util.List;

@Data
public class Game {
    private String gameId;
    private GameStatus gameStatus;
    private Integer roundCount;
    private Integer playersAmount;

    private List<Player> players;

    private List<Oilfield> oilfields;
    private List<CarsIndustry> carsIndustries;
    private List<DrillsIndustry> drillsIndustries;
    private List<PumpsIndustry> pumpsIndustries;

    private Double[] oilPrices;
}
