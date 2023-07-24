package com.ludo.Snake.and.Ladder.controller

import com.ludo.Snake.and.Ladder.Dto.StartGameResponse
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.Dto.GameConfigurationDto
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.GenericSuccessResponse
import com.ludo.Snake.and.Ladder.service.GameConfigurationService
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
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
            return new ResponseEntity<>(gameConfigResponse.getLeft(), HttpStatusCode.valueOf(gameConfigResponse.getLeft().status))
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
            return new ResponseEntity<>(response.getLeft(), HttpStatusCode.valueOf(response.getLeft().status))
        }
        log.info("[${className}][getGameConfiguration][Exit]")
        return response.get()
    }

    @MessageMapping("/startGame") // /app/startGame (messaging part)
    @SendTo("/startGame/public") // subscription part
    @PostMapping("/startGame")
    def startGame(@Payload String gameId) {
        log.info("[${className}][startGame][Enter]")
        Either<GenericErrorResponse, StartGameResponse> startGameResponse = gameConfigurationService.startGame(gameId)
        if(startGameResponse.isLeft()) {
            log.info("[${className}][startGame][Exit]")
            return new ResponseEntity<>(startGameResponse.getLeft(), HttpStatusCode.valueOf(startGameResponse.getLeft().status))
        }
        log.info("[${className}][startGame][Exit]")
        return startGameResponse.get()
    }
}
