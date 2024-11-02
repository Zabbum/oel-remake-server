package com.github.zabbum.oelremakeserver.model;

import com.github.zabbum.oelremakecomponents.Player;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StarterKit extends Player {
    private Integer playersAmount;

    public StarterKit(String name, Integer playersAmount) {
        super(name);
        this.playersAmount = playersAmount;
    }
}
