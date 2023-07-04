package com.ludo.Snake.and.Ladder.service

import com.ludo.Snake.and.Ladder.Constants
import com.ludo.Snake.and.Ladder.model.Board
import com.ludo.Snake.and.Ladder.model.Dice
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.model.GameConfigurationDto
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.GenericSuccessResponse
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.repository.GameConfigurationRepository
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

import java.util.stream.IntStream


@Slf4j
@Service
class GameConfigurationService {

    @Autowired
    GameConfigurationRepository gameConfigurationRepository

    private final className = this.class.simpleName

    Either<GenericErrorResponse, GameConfiguration> saveGameConfig(GameConfigurationDto gameConfigurationRequest) {
        log.info("[${className}][saveGameConfig][Enter]")
        Random random = new Random()
        ArrayList<IntStream> randomNumberGenerator = random.ints(3,0, Integer.MAX_VALUE).findAll()
        String gameConfigId = "game_" + randomNumberGenerator[0].toString()
        String boardId = "board_" + randomNumberGenerator[1].toString()
        String diceId = "dice_" + randomNumberGenerator[2].toString()

        GameConfiguration gameConfiguration = new GameConfiguration().tap {
            id = gameConfigId
            boardRows = gameConfigurationRequest.boardRows
            boardColumns = gameConfigurationRequest.boardColumns
            playerCount = gameConfigurationRequest.playerCount
            gameState = gameConfigurationRequest.gameState
            boardSize = gameConfigurationRequest.boardRows * gameConfigurationRequest.boardColumns
        }
        Dice dice = new Dice().tap {
            id = diceId
            count = gameConfigurationRequest.diceCount
        }
        Board board = new Board().tap {
            id = boardId
            it.dice = dice
            snakeOrLadder = gameConfigurationRequest.snakeOrLadder
        }
        gameConfiguration.board = board

        GameConfiguration gameConfigurationResponse
        try {
            gameConfigurationResponse = gameConfigurationRepository.save(gameConfiguration)
        } catch (Exception ex) {
            log.error("Exception occurred while saving game config: Exception [{}]", ex)
            log.info("[${className}][saveGameConfig][Exit]")
            return Either.left(new GenericErrorResponse().tap {
                status = 400
                reason = "Exception Occurred during saving of the Configuration"})
        }
        log.info("[${className}][saveGameConfig][Exit]")
        return Either.right(gameConfigurationResponse)
    }

    Either<GenericErrorResponse,GameConfiguration> getGameConfig(String gameId) {
        log.info("[${className}][getGameConfig][Enter]")
        Optional<GameConfiguration> gameConfigurationOptional
        try {
            gameConfigurationOptional = gameConfigurationRepository.findById(gameId)
        } catch (Exception ex) {
            log.error("Exception occurred while getting Game Configuration for id ${gameId} Exception: ", ex)
            return Either.left(new GenericErrorResponse().tap {
                status = 400
                reason = "Exception occurred while getting game configuration for id ${gameId}"
            })
        }
        if(gameConfigurationOptional.isEmpty()) {
            log.info("[${className}][getGameConfig][Exit]")
            return Either.left(new GenericErrorResponse().tap {
                status = 404
                reason = "Game configuration not found for gameId: ${gameId}"
            })
        }

        log.info("[${className}][getGameConfig][Exit]")
        return Either.right(gameConfigurationOptional.get())
    }

    Either<GenericErrorResponse, GenericSuccessResponse> startGame(String gameId) {
        log.info("[${className}][startGame][Enter]")
        Optional<GameConfiguration> optionalGameConfiguration = gameConfigurationRepository.findById(gameId)
        if(optionalGameConfiguration.isEmpty()) {
            log.error("Game with id ${gameId} not found")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Game not found"))
        }

        GameConfiguration gameConfiguration = optionalGameConfiguration.get()
        if(gameConfiguration.gameState == Constants.GameState.IN_PROGRESS) {
            log.error("Game with game id ${gameId} is already in Progress")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Game already in progress"))
        }

        if(gameConfiguration.gameState == Constants.GameState.FINISHED) {
            log.error("Game with game id ${gameId} already finished ")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Game already finished"))
        }

        try {
            gameConfiguration.setGameState(Constants.GameState.IN_PROGRESS)
            gameConfiguration = gameConfigurationRepository.save(gameConfiguration)
        } catch (Exception ex) {
            log.error("Exception occurred while saving game ${gameId}. Exception", ex)
            return Either.left(new GenericErrorResponse(status: 400, reason: "Error occurred while saving game, please try again"))
        }
        log.info("Game state updated to ${gameConfiguration.gameState}")
        log.info("[${className}][startGame][Exit]")
        return Either.right(new GenericSuccessResponse(status: 200, reason: "Game started successfully"))
    }
}
