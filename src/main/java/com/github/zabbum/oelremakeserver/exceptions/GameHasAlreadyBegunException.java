package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelrlib.game.BaseGame;

public class GameHasAlreadyBegunException extends RuntimeException {
    public GameHasAlreadyBegunException(BaseGame game) {
        super("Game " + game.getGameId() + " has already begun.");
    }
}
