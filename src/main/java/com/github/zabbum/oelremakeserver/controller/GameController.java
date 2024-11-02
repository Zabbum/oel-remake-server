package com.github.zabbum.oelremakeserver.controller;

import com.github.zabbum.oelremakecomponents.plants.industries.Cars.CarsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Drills.DrillsIndustry;
import com.github.zabbum.oelremakecomponents.plants.industries.Pumps.PumpsIndustry;
import com.github.zabbum.oelremakecomponents.plants.oilfield.Oilfield;
import com.github.zabbum.oelremakeserver.model.Game;
import com.github.zabbum.oelremakeserver.model.JoinKit;
import com.github.zabbum.oelremakeserver.model.StarterKit;
import com.github.zabbum.oelremakeserver.service.GameService;
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
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<Game> createGame(@RequestBody StarterKit starterKit) {
        log.info("Game creation request: {}", starterKit);
        return ResponseEntity.ok(gameService.createGame(starterKit, starterKit.getPlayersAmount()));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(@RequestBody JoinKit joinKit) {
        log.info("Game connection request: {}", joinKit);
        return ResponseEntity.ok(gameService.connectToGame(joinKit, joinKit.getGameId()));
    }

    @PostMapping("/getOilfields")
    public ResponseEntity<List<Oilfield>> getOilfields(@RequestBody String gameId) {
        return ResponseEntity.ok(gameService.getOilfields(gameId));
    }

    @PostMapping("/getCarsIndustries")
    public ResponseEntity<List<CarsIndustry>> getCarsIndustries(@RequestBody String gameId) {
        return ResponseEntity.ok(gameService.getCarsIndustries(gameId));
    }

    @PostMapping("/getDrillsIndustries")
    public ResponseEntity<List<DrillsIndustry>> getDrillsIndustries(@RequestBody String gameId) {
        return ResponseEntity.ok(gameService.getDrillsIndustries(gameId));
    }

    @PostMapping("/getPumpsIndustries")
    public ResponseEntity<List<PumpsIndustry>> getPumpsIndustries(@RequestBody String gameId) {
        return ResponseEntity.ok(gameService.getPumpsIndustries(gameId));
    }
}
