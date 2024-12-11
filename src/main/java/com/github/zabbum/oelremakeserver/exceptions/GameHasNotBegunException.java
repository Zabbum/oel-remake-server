package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelrlib.game.BaseGame;

public class GameHasNotBegunException extends RuntimeException {
    public GameHasNotBegunException(BaseGame game) {
        super("Game " + game.getGameId() + " has not begun.");
    }
}
