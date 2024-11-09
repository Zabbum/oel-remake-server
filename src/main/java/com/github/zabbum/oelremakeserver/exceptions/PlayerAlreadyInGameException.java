package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakeserver.model.Game;

public class PlayerAlreadyInGameException extends RuntimeException {
    public PlayerAlreadyInGameException(String playerName, Game game) {
        super("Player " + playerName + " is already in a game: " + game.getGameId());
    }
}
