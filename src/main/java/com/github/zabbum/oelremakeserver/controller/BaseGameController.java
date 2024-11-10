package com.github.zabbum.oelremakeserver.controller;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.game.BaseGame;
import com.github.zabbum.oelremakecomponents.plants.industries.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.model.kits.*;
import com.github.zabbum.oelremakeserver.service.BaseGameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/baseGame")
public class BaseGameController {

    private final BaseGameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RuntimeException> handleAnotherPlayersTurnException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/start")
    public ResponseEntity<BaseGame> createGame(@RequestBody StarterKit starterKit) {
        log.info("Game creation request: {}", starterKit);
        return ResponseEntity.ok(gameService.createGame(starterKit.getPlayerName(), starterKit.getPlayersAmount()));
    }

    @PostMapping("/connect")
    public ResponseEntity<BaseGame> connectToGame(@RequestBody JoinKit joinKit) {
        log.info("Game connection request: {}", joinKit);
        BaseGame game = gameService.connectToGame(joinKit.getPlayerName(), joinKit.getGameId());
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/getOilfields")
    public ResponseEntity<List<Oilfield>> getOilfields(@RequestBody GameIdKit gameIdKit) {
        log.info("Get oilfields request");
        return ResponseEntity.ok(gameService.getOilfields(gameIdKit.getGameId()));
    }

    @PostMapping("/getCarsIndustries")
    public ResponseEntity<List<CarsIndustry>> getCarsIndustries(@RequestBody GameIdKit gameIdKit) {
        log.info("Get cars industries request");
        return ResponseEntity.ok(gameService.getCarsIndustries(gameIdKit.getGameId()));
    }

    @PostMapping("/getDrillsIndustries")
    public ResponseEntity<List<DrillsIndustry>> getDrillsIndustries(@RequestBody GameIdKit gameIdKit) {
        log.info("Get drills industries request");
        return ResponseEntity.ok(gameService.getDrillsIndustries(gameIdKit.getGameId()));
    }

    @PostMapping("/getPumpsIndustries")
    public ResponseEntity<List<PumpsIndustry>> getPumpsIndustries(@RequestBody GameIdKit gameIdKit) {
        log.info("Get pumps industries request");
        return ResponseEntity.ok(gameService.getPumpsIndustries(gameIdKit.getGameId()));
    }

    @PostMapping("/getPlayer")
    public ResponseEntity<Player> getPlayer(@RequestBody PlayerInfoKit playerInfoKit) {
        log.info("Get player request: {}", playerInfoKit);
        return ResponseEntity.ok(gameService.getPlayer(playerInfoKit.getGameId(), playerInfoKit.getPlayerId()));
    }

    @PostMapping("/getGame")
    public ResponseEntity<BaseGame> getGame(@RequestBody String gameId) {
        log.info("Get game request: {}", gameId);
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @PostMapping("/buyOilfield")
    public ResponseEntity<BaseGame> buyOilfield(@RequestBody BuyOilfieldKit buyOilfieldKit) {
        log.info("Buy oilfield request: {}", buyOilfieldKit);
        BaseGame game = gameService.buyOilfield(
                buyOilfieldKit.getGameId(), buyOilfieldKit.getPlayerId(), buyOilfieldKit.getOilfieldId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/buyIndustry")
    public ResponseEntity<BaseGame> buyIndustry(@RequestBody BuyIndustryKit buyIndustryKit) throws ClassNotFoundException {
        log.info("Buy industry request: {}", buyIndustryKit);
        BaseGame game = gameService.buyIndustry(
                buyIndustryKit.getGameId(), buyIndustryKit.getPlayerId(), buyIndustryKit.getIndustryClassName(),
                buyIndustryKit.getIndustryId(), buyIndustryKit.getProductPrice()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/buyProducts")
    public ResponseEntity<BaseGame> buyProducts(@RequestBody BuyProductsKit buyProductsKit) throws ClassNotFoundException {
        log.info("Buy products request: {}", buyProductsKit);
        BaseGame game = gameService.buyProducts(
                buyProductsKit.getGameId(), buyProductsKit.getPlayerId(), buyProductsKit.getIndustryClassName(),
                buyProductsKit.getIndustryId(), buyProductsKit.getProductAmount(), buyProductsKit.getOilfieldId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/changePrices")
    public ResponseEntity<BaseGame> changePrices(@RequestBody ChangePricesKit changePricesKit) throws ClassNotFoundException {
        log.info("Change prices request: {}", changePricesKit);
        BaseGame game = gameService.changePrices(
                changePricesKit.getGameId(), changePricesKit.getPlayerId(), changePricesKit.getIndustryClassName(),
                changePricesKit.getIndustryId(), changePricesKit.getNewPrice()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/sabotage")
    public ResponseEntity<BaseGame> doSabotage(@RequestBody SabotageKit sabotageKit) throws ClassNotFoundException {
        log.info("Do sabotage request: {}", sabotageKit);
        BaseGame game = gameService.doSabotage(
                sabotageKit.getGameId(), sabotageKit.getPlayerId(), sabotageKit.getPlantClassName(),
                sabotageKit.getPlantId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

}
