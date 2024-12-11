package com.github.zabbum.oelremakeserver.exceptions;

import com.github.zabbum.oelrlib.game.BaseGame;

public class AnotherPlayersTurnException extends RuntimeException {
    public AnotherPlayersTurnException(Integer playerId, BaseGame game) {
        super("It is currently not player " + playerId + " turn. Player " + game.getCurrentPlayerTurn() + " is having turn!");
    }
}
