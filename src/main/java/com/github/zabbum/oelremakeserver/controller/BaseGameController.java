package com.github.zabbum.oelremakeserver.controller;

import com.github.zabbum.oelremakecomponents.Player;
import com.github.zabbum.oelremakecomponents.game.BaseGame;
import com.github.zabbum.oelremakecomponents.plants.industries.AbstractIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.model.kits.*;
import com.github.zabbum.oelremakeserver.service.BaseGameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/baseGame")
public class BaseGameController {

    private final BaseGameService gameService;

    @PostMapping("/start")
    public ResponseEntity<BaseGame> createGame(@RequestBody StarterKit starterKit) {
        log.info("Game creation request: {}", starterKit);
        return ResponseEntity.ok(gameService.createGame(starterKit.getPlayerName(), starterKit.getPlayersAmount()));
    }

    @PostMapping("/connect")
    public ResponseEntity<BaseGame> connectToGame(@RequestBody JoinKit joinKit) {
        log.info("Game connection request: {}", joinKit);
        return ResponseEntity.ok(gameService.connectToGame(joinKit.getPlayerName(), joinKit.getGameId()));
    }

    @PostMapping("/getOilfields")
    public ResponseEntity<List<Oilfield>> getOilfields(@RequestBody String gameId) {
        log.info("Get oilfields request");
        return ResponseEntity.ok(gameService.getOilfields(gameId));
    }

    @PostMapping("/getCarsIndustries")
    public ResponseEntity<List<CarsIndustry>> getCarsIndustries(@RequestBody String gameId) {
        log.info("Get cars industries request");
        return ResponseEntity.ok(gameService.getCarsIndustries(gameId));
    }

    @PostMapping("/getDrillsIndustries")
    public ResponseEntity<List<DrillsIndustry>> getDrillsIndustries(@RequestBody String gameId) {
        log.info("Get drills industries request");
        return ResponseEntity.ok(gameService.getDrillsIndustries(gameId));
    }

    @PostMapping("/getPumpsIndustries")
    public ResponseEntity<List<PumpsIndustry>> getPumpsIndustries(@RequestBody String gameId) {
        log.info("Get pumps industries request");
        return ResponseEntity.ok(gameService.getPumpsIndustries(gameId));
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
    public ResponseEntity<Oilfield> buyOilfield(@RequestBody BuyOilfieldKit buyOilfieldKit) {
        log.info("Buy oilfield request: {}", buyOilfieldKit);
        return ResponseEntity.ok(gameService.buyOilfield(
                buyOilfieldKit.getGameId(), buyOilfieldKit.getPlayerId(), buyOilfieldKit.getOilfieldId()
        ));
    }

    @PostMapping("/buyIndustry")
    public ResponseEntity<AbstractIndustry> buyIndustry(@RequestBody BuyIndustryKit buyIndustryKit) throws ClassNotFoundException {
        log.info("Buy industry request: {}", buyIndustryKit);
        return ResponseEntity.ok(gameService.buyIndustry(
                buyIndustryKit.getGameId(), buyIndustryKit.getPlayerId(), buyIndustryKit.getIndustryClassName(),
                buyIndustryKit.getIndustryId(), buyIndustryKit.getProductPrice()
        ));
    }

    @PostMapping("/buyProducts")
    public ResponseEntity<Oilfield> buyProducts(@RequestBody BuyProductsKit buyProductsKit) throws ClassNotFoundException {
        log.info("Buy products request: {}", buyProductsKit);
        return ResponseEntity.ok(gameService.buyProducts(
                buyProductsKit.getGameId(), buyProductsKit.getPlayerId(), buyProductsKit.getIndustryClassName(),
                buyProductsKit.getIndustryId(), buyProductsKit.getProductAmount(), buyProductsKit.getOilfieldId()
        ));
    }

    @PostMapping("/changePrices")
    public ResponseEntity<BaseGame> changePrices(@RequestBody ChangePricesKit changePricesKit) throws ClassNotFoundException {
        log.info("Change prices request: {}", changePricesKit);
        return ResponseEntity.ok(gameService.changePrices(
                changePricesKit.getGameId(), changePricesKit.getPlayerId(), changePricesKit.getIndustryClassName(),
                changePricesKit.getIndustryId(), changePricesKit.getNewPrice()
        ));
    }
}
