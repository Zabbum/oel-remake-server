package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakeserver.model.Game;

public class PlayerAlreadyInGameException extends RuntimeException {
    public PlayerAlreadyInGameException(Player player, Game game) {
        super("Player " + player.getName() + " is already in a game: " + game.getGameId());
    }
}
