package com.ludo.Snake.and.Ladder.controller

import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.Dto.GameConfigurationDto
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.GenericSuccessResponse
import com.ludo.Snake.and.Ladder.service.GameConfigurationService
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping("/configure")
class GameConfigurationController {

    @Autowired
    GameConfigurationService gameConfigurationService

    private final className = this.class.simpleName


//    @GetMapping("/game")
//    static Model gameConfigSetup(Model model) {
//        return "add-gameConfiguration"
//    }

    @PostMapping("/game")
    def saveGameConfig(@RequestBody GameConfigurationDto gameConfigurationRequest) {
        log.info("[${className}][gameConfigSetup][Enter]")
        log.info("requestBody: ${gameConfigurationRequest.dump()}")
        Either<GenericErrorResponse, GameConfiguration> gameConfigResponse = gameConfigurationService.saveGameConfig(gameConfigurationRequest)
        if(gameConfigResponse.isLeft()) {
            log.info("[${className}][gameConfigSetup][Exit]")
            return gameConfigResponse.getLeft()
        }
        log.info("[${className}][gameConfigSetup][Exit]")
        return gameConfigResponse.get()
    }

    @GetMapping("/getGame")
    def getGameConfig(@RequestParam String gameId) {
        log.info("[${className}][getGameConfiguration][Enter]")
        log.info("gameId: ${gameId}")
        Either<GenericErrorResponse, GameConfiguration> response = gameConfigurationService.getGameConfig(gameId)

        if(response.isLeft()) {
            log.info("[${className}][getGameConfiguration][Exit]")
            return response.getLeft()
        }
        log.info("[${className}][getGameConfiguration][Exit]")
        return response.get()
    }

    @PostMapping("/startGame")
    def startGame(@RequestParam String gameId) {
        log.info("[${className}][startGame][Enter]")
        Either<GenericErrorResponse, GenericSuccessResponse> startGameResponse = gameConfigurationService.startGame(gameId)
        if(startGameResponse.isLeft()) {
            log.info("[${className}][startGame][Exit]")
            return startGameResponse.getLeft()
        }
        log.info("[${className}][startGame][Exit]")
        return startGameResponse.get()
    }
}
