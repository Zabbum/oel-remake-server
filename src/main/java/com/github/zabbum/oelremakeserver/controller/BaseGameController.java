package com.github.zabbum.oelremakeserver.controller;

import com.github.zabbum.oelrlib.Player;
import com.github.zabbum.oelrlib.game.BaseGame;
import com.github.zabbum.oelrlib.plants.industries.CarsIndustry;
import com.github.zabbum.oelrlib.plants.industries.DrillsIndustry;
import com.github.zabbum.oelrlib.plants.industries.PumpsIndustry;
import com.github.zabbum.oelrlib.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.service.BaseGameService;
import com.github.zabbum.oelrlib.requests.*;
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
    public ResponseEntity<BaseGame> createGame(@RequestBody StarterRequest starterRequest) {
        log.info("Game creation request: {}", starterRequest);
        return ResponseEntity.ok(gameService.createGame(starterRequest.getPlayerName(), starterRequest.getPlayersAmount()));
    }

    @PostMapping("/connect")
    public ResponseEntity<BaseGame> connectToGame(@RequestBody JoinRequest joinRequest) {
        log.info("Game connection request: {}", joinRequest);
        BaseGame game = gameService.connectToGame(joinRequest.getPlayerName(), joinRequest.getGameId());
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/getOilfields")
    public ResponseEntity<List<Oilfield>> getOilfields(@RequestBody GameIdRequest gameIdRequest) {
        log.info("Get oilfields request");
        return ResponseEntity.ok(gameService.getOilfields(gameIdRequest.getGameId()));
    }

    @PostMapping("/getCarsIndustries")
    public ResponseEntity<List<CarsIndustry>> getCarsIndustries(@RequestBody GameIdRequest gameIdRequest) {
        log.info("Get cars industries request");
        return ResponseEntity.ok(gameService.getCarsIndustries(gameIdRequest.getGameId()));
    }

    @PostMapping("/getDrillsIndustries")
    public ResponseEntity<List<DrillsIndustry>> getDrillsIndustries(@RequestBody GameIdRequest gameIdRequest) {
        log.info("Get drills industries request");
        return ResponseEntity.ok(gameService.getDrillsIndustries(gameIdRequest.getGameId()));
    }

    @PostMapping("/getPumpsIndustries")
    public ResponseEntity<List<PumpsIndustry>> getPumpsIndustries(@RequestBody GameIdRequest gameIdRequest) {
        log.info("Get pumps industries request");
        return ResponseEntity.ok(gameService.getPumpsIndustries(gameIdRequest.getGameId()));
    }

    @PostMapping("/getPlayer")
    public ResponseEntity<Player> getPlayer(@RequestBody PlayerInfoRequest playerInfoRequest) {
        log.info("Get player request: {}", playerInfoRequest);
        return ResponseEntity.ok(gameService.getPlayer(playerInfoRequest.getGameId(), playerInfoRequest.getPlayerId()));
    }

    @PostMapping("/getGame")
    public ResponseEntity<BaseGame> getGame(@RequestBody String gameId) {
        log.info("Get game request: {}", gameId);
        return ResponseEntity.ok(gameService.getGame(gameId));
    }

    @PostMapping("/buyOilfield")
    public ResponseEntity<BaseGame> buyOilfield(@RequestBody BuyOilfieldRequest buyOilfieldRequest) {
        log.info("Buy oilfield request: {}", buyOilfieldRequest);
        BaseGame game = gameService.buyOilfield(
                buyOilfieldRequest.getGameId(), buyOilfieldRequest.getPlayerId(), buyOilfieldRequest.getOilfieldId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/buyIndustry")
    public ResponseEntity<BaseGame> buyIndustry(@RequestBody BuyIndustryRequest buyIndustryRequest) throws ClassNotFoundException {
        log.info("Buy industry request: {}", buyIndustryRequest);
        BaseGame game = gameService.buyIndustry(
                buyIndustryRequest.getGameId(), buyIndustryRequest.getPlayerId(), buyIndustryRequest.getIndustryClassName(),
                buyIndustryRequest.getIndustryId(), buyIndustryRequest.getProductPrice()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/buyProducts")
    public ResponseEntity<BaseGame> buyProducts(@RequestBody BuyProductsRequest buyProductsRequest) throws ClassNotFoundException {
        log.info("Buy products request: {}", buyProductsRequest);
        BaseGame game = gameService.buyProducts(
                buyProductsRequest.getGameId(), buyProductsRequest.getPlayerId(), buyProductsRequest.getIndustryClassName(),
                buyProductsRequest.getIndustryId(), buyProductsRequest.getProductAmount(), buyProductsRequest.getOilfieldId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/changePrices")
    public ResponseEntity<BaseGame> changePrices(@RequestBody ChangePricesRequest changePricesRequest) throws ClassNotFoundException {
        log.info("Change prices request: {}", changePricesRequest);
        BaseGame game = gameService.changePrices(
                changePricesRequest.getGameId(), changePricesRequest.getPlayerId(), changePricesRequest.getIndustryClassName(),
                changePricesRequest.getIndustryId(), changePricesRequest.getNewPrice()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/sabotage")
    public ResponseEntity<BaseGame> doSabotage(@RequestBody SabotageRequest sabotageRequest) throws ClassNotFoundException {
        log.info("Do sabotage request: {}", sabotageRequest);
        BaseGame game = gameService.doSabotage(
                sabotageRequest.getGameId(), sabotageRequest.getPlayerId(), sabotageRequest.getPlantClassName(),
                sabotageRequest.getPlantId()
        );
        messagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

}
