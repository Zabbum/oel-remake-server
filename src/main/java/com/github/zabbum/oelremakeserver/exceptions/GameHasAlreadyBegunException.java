package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelremakeserver.model.Game;

public class GameHasAlreadyBegunException extends RuntimeException {
    public GameHasAlreadyBegunException(Game game) {
        super("Game " + game.getGameId() + " has already begun.");
    }
}
