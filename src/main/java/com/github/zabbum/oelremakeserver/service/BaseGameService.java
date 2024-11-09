package com.github.zabbum.oelremakeserver.service;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.game.BaseGame;
import com.github.zabbum.oelremakecomponents.game.GameStatus;
import com.github.zabbum.oelremakecomponents.plants.AbstractPlant;
import com.github.zabbum.oelremakecomponents.plants.industries.AbstractIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.exceptions.*;
import com.github.zabbum.oelremakeserver.model.SabotageSuccess;
import com.github.zabbum.oelremakeserver.operations.*;
import com.github.zabbum.oelremakeserver.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

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
        game.setCurrentRound(0);
        game.setRoundCount(20);
        game.setPlayersAmount(playersAmount);

        // Set player 0
        Player player = new Player(playerName, 0);
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
        Player player = new Player(playerName, game.getPlayers().size() - 1);
        game.getPlayers().add(player);

        // If game is full, change status
        if (game.getPlayers().size() == game.getPlayersAmount()) {
            game.setGameStatus(GameStatus.IN_PROGRESS);
            game.setCurrentPlayerTurn(0);
        }

        return game;
    }

    public void endTurn(BaseGame game) throws GameDoesNotExistException {
        game.endCurrentPlayerTurn();
        if (game.getCurrentPlayerTurn() == game.getPlayers().size()) {
            game.setGameStatus(GameStatus.FINISHED);
        }
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

    public Oilfield buyOilfield(String gameId, Integer playerId, Integer oilfieldId) {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // If game has not begun, throw an exception
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new GameHasNotBegunException(game);
        }

        // If another player is having turn, throw an exception
        if (!Objects.equals(game.getCurrentPlayerTurn(), playerId)) {
            throw new AnotherPlayersTurnException(playerId, game);
        }

        List<Oilfield> oilfields = game.getOilfields();
        Oilfield selectedOilfield = oilfields.get(oilfieldId);

        if (selectedOilfield.isBought()) {
            throw new PlantAlreadyBoughtException(selectedOilfield);
        }

        // Note purchase
        selectedOilfield.setOwnership(player);
        player.decreaseBalance(selectedOilfield.getPlantPrice());

        endTurn(game);

        return selectedOilfield;
    }

    public AbstractIndustry buyIndustry(String gameId, Integer playerId, String industryClassName, Integer industryId, Integer productPrice) throws ClassNotFoundException {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // If game has not begun, throw an exception
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new GameHasNotBegunException(game);
        }

        // If another player is having turn, throw an exception
        if (!Objects.equals(game.getCurrentPlayerTurn(), playerId)) {
            throw new AnotherPlayersTurnException(playerId, game);
        }

        // Verification of data received
        Class<?> tmpClass = Class.forName(industryClassName);
        if (tmpClass.isAssignableFrom(AbstractIndustry.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked") Class<? extends AbstractIndustry> industryClass = (Class<? extends AbstractIndustry>) tmpClass;
        @SuppressWarnings("unchecked") List<? extends AbstractIndustry> industries = (List<? extends AbstractIndustry>) game.getPlantsList(industryClass);
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

        endTurn(game);

        return selectedIndustry;
    }

    public Oilfield buyProducts(String gameId, Integer playerId, String industryClassName, Integer industryId, Integer productAmount, Integer oilfieldId) throws ClassNotFoundException {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // If game has not begun, throw an exception
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new GameHasNotBegunException(game);
        }

        // If another player is having turn, throw an exception
        if (!Objects.equals(game.getCurrentPlayerTurn(), playerId)) {
            throw new AnotherPlayersTurnException(playerId, game);
        }

        List<Oilfield> oilfields = game.getOilfields();
        Oilfield selectedOilfield = oilfields.get(oilfieldId);

        // Verification of data received
        Class<?> tmpClass = Class.forName(industryClassName);
        if (tmpClass.isAssignableFrom(AbstractIndustry.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked") Class<? extends AbstractIndustry> industryClass = (Class<? extends AbstractIndustry>) tmpClass;
        @SuppressWarnings("unchecked") List<? extends AbstractIndustry> industries = (List<? extends AbstractIndustry>) game.getPlantsList(industryClass);
        AbstractIndustry selectedIndustry = industries.get(industryId);

        if (!selectedIndustry.isBought()) {
            throw new IllegalArgumentException("Selected industry is not bought");
        }

        // Take actions
        selectedIndustry.buyProducts(productAmount);
        player.decreaseBalance(productAmount * selectedIndustry.getProductPrice());
        if (selectedIndustry.getOwnership() == player) {
            player.increaseBalance(0.2 * productAmount * selectedIndustry.getProductPrice());
        } else {
            selectedIndustry.getOwnership().increaseBalance(productAmount * selectedIndustry.getProductPrice());
        }

        selectedOilfield.addProductAmount(selectedIndustry.getClass(), productAmount);

        endTurn(game);

        return selectedOilfield;
    }

    public BaseGame changePrices(String gameId, Integer playerId, String industryClassName, Integer industryId, Integer newPrice) throws ClassNotFoundException {

        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // If game has not begun, throw an exception
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new GameHasNotBegunException(game);
        }

        // If another player is having turn, throw an exception
        if (!Objects.equals(game.getCurrentPlayerTurn(), playerId)) {
            throw new AnotherPlayersTurnException(playerId, game);
        }

        // Verification of data received
        Class<?> tmpClass = Class.forName(industryClassName);
        if (tmpClass.isAssignableFrom(AbstractIndustry.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked") Class<? extends AbstractIndustry> industryClass = (Class<? extends AbstractIndustry>) tmpClass;
        @SuppressWarnings("unchecked") List<? extends AbstractIndustry> industries = (List<? extends AbstractIndustry>) game.getPlantsList(industryClass);
        AbstractIndustry selectedIndustry = industries.get(industryId);

        if (selectedIndustry.getOwnership() != player) {
            throw new PlayerIsNotOwnerException(player, selectedIndustry);
        }

        // Take actions
        selectedIndustry.setProductPrice(newPrice);

        endTurn(game);

        return game;
    }

    public SabotageSuccess doSabotage(String gameId, Integer playerId, String plantClassName, Integer plantId) throws ClassNotFoundException {
        BaseGame game = GameStorage.getInstance().getGames().get(gameId);
        Player player = game.getPlayers().get(playerId);

        // If game has not begun, throw an exception
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            throw new GameHasNotBegunException(game);
        }

        // If another player is having turn, throw an exception
        if (!Objects.equals(game.getCurrentPlayerTurn(), playerId)) {
            throw new AnotherPlayersTurnException(playerId, game);
        }

        // Verification of data received
        Class<?> tmpClass = Class.forName(plantClassName);
        if (tmpClass.isAssignableFrom(AbstractPlant.class)) {
            throw new ClassIsNotCorrect(tmpClass);
        }

        @SuppressWarnings("unchecked") Class<? extends AbstractPlant> plantClass = (Class<? extends AbstractPlant>) tmpClass;
        List<? extends AbstractPlant> plants = game.getPlantsList(plantClass);
        AbstractPlant selectedPlant = plants.get(plantId);

        // Do sabotage depending on what plant type is chosen
        SabotageSuccess sabotageSuccess = switch (tmpClass.getSimpleName()) {
            case "Oilfield" -> oilfieldSabotage((Oilfield) selectedPlant, player);
            case "CarsIndustry", "DrillsIndustry", "PumpsIndustry" ->
                    industrySabotage((AbstractIndustry) selectedPlant, player);
            default -> null;
        };

        endTurn(game);

        return sabotageSuccess;
    }

    private SabotageSuccess oilfieldSabotage(Oilfield oilfield, Player player) {
        Random random = new Random();
        boolean isSucceed = random.nextBoolean();
        int isSucceed2 = random.nextInt(3);

        if (!isSucceed) {
            return SabotageSuccess.FAILURE;
        }

        // Generate fees
        int fees1 = random.nextInt(40000) + 40000;
        int fees2 = random.nextInt(40000) + 1;

        player.decreaseBalance(fees1 + fees2);

        if (!(isSucceed2 == 2)) {
            return SabotageSuccess.MONEYTAKEN;
        }

        // Reset oilfield
        oilfield.setPlantPrice(random.nextInt(50000) + 30001);
        oilfield.setTotalOilAmount(random.nextInt(200000) + 1);
        oilfield.setOwnership(null);
        oilfield.setExploitable(false);
        oilfield.setRequiredDepth(random.nextInt(4500) + 1);
        oilfield.setPumpsAmount(0);
        oilfield.setCarsAmount(0);
        oilfield.setDrillsAmount(0);
        oilfield.setCurrentDepth(0);
        oilfield.setOilExtracted(0);
        oilfield.setOilAvailabletoSell(0);

        return SabotageSuccess.SUCCESS;
    }

    private SabotageSuccess industrySabotage(AbstractIndustry industry, Player player) {

        double[] results = new double[]{0.5, -0.2, 0.4, -0.1, 0.3, -0.3, 0.1, -0.4, 0.2, -0.5};

        Random random = new Random();
        double result = results[random.nextInt(results.length)] + 1;

        // Take action
        player.decreaseBalance(industry.getPlantPrice() * result);

        if (result < 1) {
            industry.setPlantPrice(random.nextInt(100000) + 1);
            industry.setProductPrice(0);
            industry.setProductsAmount(industry.getPlantPrice() / 10000);
            industry.setOwnership(null);

            return SabotageSuccess.SUCCESS;
        }

        return SabotageSuccess.FAILURE;
    }
}
