package com.github.zabbum.oelremakeserver.exceptions;

public class GameDoesNotExistException extends RuntimeException {
    public GameDoesNotExistException(String gameId) {
        super("Game with ID: " + gameId + " does not exist.");
    }
}
