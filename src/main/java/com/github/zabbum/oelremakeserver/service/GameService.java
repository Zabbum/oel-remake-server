package com.github.zabbum.oelremakeserver.service;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.plants.industries.Cars.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Drills.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Pumps.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.exceptions.GameDoesNotExistException;
import com.github.zabbum.oelremakeserver.exceptions.GameHasAlreadyBegunException;
import com.github.zabbum.oelremakeserver.exceptions.PlayerAlreadyInGameException;
import com.github.zabbum.oelremakeserver.model.Game;
import com.github.zabbum.oelremakeserver.model.GameStatus;
import com.github.zabbum.oelremakeserver.operations.*;
import com.github.zabbum.oelremakeserver.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameService {

    /**
     * Create new game
     *
     * @return new game
     */
    public Game createGame(Player player, Integer playersAmount) {
        Game game = new Game();

        // Set round count
        game.setGameId(UUID.randomUUID().toString());
        game.setGameStatus(GameStatus.NEW);
        game.setRoundCount(20);
        game.setPlayersAmount(playersAmount);

        // Set player 0
        game.setPlayers(new ArrayList<>());
        game.getPlayers().add(player);

        // Initialize plants
        game.setOilfields(OilfieldOperations.initialize());
        game.setCarsIndustries(CarsIndustryOperations.initialize());
        game.setDrillsIndustries(DrillsIndustryOperations.initialize());
        game.setPumpsIndustries(PumpsIndustryOperations.initialize());

        // Generate oil prices
        game.setOilPrices(OilOperations.generatePrices(game.getRoundCount()));

        // Store game in GameStorage
        GameStorage.getInstance().putGame(game);

        return game;
    }

    public Game connectToGame(Player player, String gameId) throws GameDoesNotExistException {
        // If game does not exist, throw an exception
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameDoesNotExistException(gameId);
        }

        // Get game by gameId
        Game game = GameStorage.getInstance().getGames().get(gameId);

        // If game is full, throw exception
        if (game.getGameStatus() != GameStatus.NEW) {
            throw new GameHasAlreadyBegunException(game);
        }
        // If player already is in this game, throw exception
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(player.getName())) {
                throw new PlayerAlreadyInGameException(player, game);
            }
        }

        // If game is full, change status
        if (game.getPlayers().size() == game.getPlayersAmount() ) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
        }

        return game;
    }

    public List<Oilfield> getOilfields(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game.getOilfields();
    }

    public List<CarsIndustry> getCarsIndustries(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game.getCarsIndustries();
    }

    public List<DrillsIndustry> getDrillsIndustries(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game.getDrillsIndustries();
    }

    public List<PumpsIndustry> getPumpsIndustries(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        return game.getPumpsIndustries();
    }
}
