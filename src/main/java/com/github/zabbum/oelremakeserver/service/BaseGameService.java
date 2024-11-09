package com.github.zabbum.oelremakeserver.service;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.plants.industries.AbstractIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.exceptions.*;
import com.github.zabbum.oelremakecomponents.game.BaseGame;
import com.github.zabbum.oelremakecomponents.game.GameStatus;
import com.github.zabbum.oelremakeserver.operations.*;
import com.github.zabbum.oelremakeserver.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BaseGameService {

    /**
     * Create new game
     *
     * @return new game
     */
    public BaseGame createGame(String playerName, Integer playersAmount) {
        BaseGame game = new BaseGame();

        // Set round count
        game.setGameId(UUID.randomUUID().toString());
        game.setGameStatus(GameStatus.NEW);
        game.setRoundCount(20);
        game.setPlayersAmount(playersAmount);

        // Set player 0
        Player player = new Player(playerName);
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

    public BaseGame connectToGame(String playerName, String gameId) throws GameDoesNotExistException {
        // If game does not exist, throw an exception
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new GameDoesNotExistException(gameId);
        }

        // Get game by gameId
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);

        // If game is in progress, throw exception
        if (game.getGameStatus() != GameStatus.NEW) {
            throw new GameHasAlreadyBegunException(game);
        }
        // If player already is in this game, throw exception
        for (Player p : game.getPlayers()) {
            if (p.getName().equals(playerName)) {
                throw new PlayerAlreadyInGameException(playerName, game);
            }
        }

        // Add player to game
        Player player = new Player(playerName);
        game.getPlayers().add(player);

        // If game is full, change status
        if (game.getPlayers().size() == game.getPlayersAmount()) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
        }

        return game;
    }

    public List<Oilfield> getOilfields(String gameId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        return game.getOilfields();
    }

    public List<CarsIndustry> getCarsIndustries(String gameId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        return game.getCarsIndustries();
    }

    public List<DrillsIndustry> getDrillsIndustries(String gameId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        return game.getDrillsIndustries();
    }

    public List<PumpsIndustry> getPumpsIndustries(String gameId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        return game.getPumpsIndustries();
    }

    public Player getPlayer(String gameId, Integer playerId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        return game.getPlayers().get(playerId);
    }

    public BaseGame getGame(String gameId) {
        return GameStorage.getInstance().getGames().get(gameId);
    }

    public Oilfield buyOilfield(
            String gameId, Integer playerId, Integer oilfieldId
    ) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        List<Oilfield> oilfields = game.getOilfields();
        Oilfield selectedOilfield = oilfields.get(oilfieldId);

        if (selectedOilfield.isBought()) {
            throw new PlantAlreadyBoughtException(selectedOilfield);
        }

        // Note purchase
        selectedOilfield.setOwnership(player);
        player.decreaseBalance(selectedOilfield.getPlantPrice());

        return selectedOilfield;
    }

    public AbstractIndustry buyIndustry(
            String gameId, Integer playerId, String industryClassName, Integer industryId,
            Integer productPrice
    )
            throws ClassNotFoundException {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // Verification of data received
        Class<?> tmpClass = Class.forName(industryClassName);
        if (tmpClass.isAssignableFrom(AbstractIndustry.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked")
        Class<? extends AbstractIndustry> industryClass = (Class<? extends AbstractIndustry>) tmpClass;
        @SuppressWarnings("unchecked")
        List<? extends AbstractIndustry> industries = (List<? extends AbstractIndustry>) game.getPlantsList(industryClass);
        AbstractIndustry selectedIndustry = industries.get(industryId);

        if (selectedIndustry.isBought()) {
            throw new PlantAlreadyBoughtException(selectedIndustry);
        }

        if (productPrice > selectedIndustry.getMaxProductPrice()) {
            throw new IllegalArgumentException("Product price exceeds maximum price");
        }

        // Note purchase
        selectedIndustry.setOwnership(player);
        player.decreaseBalance(selectedIndustry.getPlantPrice());
        selectedIndustry.setProductPrice(productPrice);

        return selectedIndustry;
    }

    public Oilfield buyProducts(
            String gameId, Integer playerId, String industryClassName, Integer industryId, Integer productAmount,
            Integer oilfieldId
    ) throws ClassNotFoundException {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);
        List<Oilfield> oilfields = game.getOilfields();
        Oilfield selectedOilfield = oilfields.get(oilfieldId);

        // Verification of data received
        Class<?> tmpClass = Class.forName(industryClassName);
        if (tmpClass.isAssignableFrom(AbstractIndustry.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked")
        Class<? extends AbstractIndustry> industryClass = (Class<? extends AbstractIndustry>) tmpClass;
        @SuppressWarnings("unchecked")
        List<? extends AbstractIndustry> industries = (List<? extends AbstractIndustry>) game.getPlantsList(industryClass);
        AbstractIndustry selectedIndustry = industries.get(industryId);

        if (!selectedIndustry.isBought()) {
            throw new IllegalArgumentException("Selected industry is not bought");
        }

        // Take actions
        selectedIndustry.buyProducts(productAmount);
        player.decreaseBalance(productAmount * selectedIndustry.getProductPrice());
        if (selectedIndustry.getOwnership() == player) {
            player.increaseBalance(0.2 * productAmount * selectedIndustry.getProductPrice());
        }
        else {
            selectedIndustry.getOwnership().increaseBalance(productAmount * selectedIndustry.getProductPrice());
        }

        selectedOilfield.addProductAmount(selectedIndustry.getClass(), productAmount);

        return selectedOilfield;
    }
}
