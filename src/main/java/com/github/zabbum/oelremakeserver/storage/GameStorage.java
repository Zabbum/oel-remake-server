package com.github.zabbum.oelremakeserver.storage;

import com.github.zabbum.oelrlib.game.BaseGame;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {
    private static Map<String, BaseGame> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, BaseGame> getGames() {
        return games;
    }

    public void putGame(BaseGame game) {
        games.put(game.getGameId(), game);
    }
}
