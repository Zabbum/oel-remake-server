package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelrlib.game.BaseGame;

public class PlayerAlreadyInGameException extends RuntimeException {
    public PlayerAlreadyInGameException(String playerName, BaseGame game) {
        super("Player " + playerName + " is already in a game: " + game.getGameId());
    }
}
