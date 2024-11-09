package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakecomponents.plants.AbstractPlant;

public class PlantAlreadyBoughtException extends RuntimeException {
    public PlantAlreadyBoughtException(AbstractPlant plant) {
        super("Plant \"" + plant.getName() + "\" is already bought");
    }
}
