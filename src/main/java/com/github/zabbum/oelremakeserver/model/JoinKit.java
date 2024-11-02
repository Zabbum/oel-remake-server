package com.github.zabbum.oelremakeserver.model;

import com.github.zabbum.oelremakecomponents.Player;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinKit extends Player {
    private String gameId;

    public JoinKit(String name, String gameId) {
        super(name);
        this.gameId = gameId;
    }
}