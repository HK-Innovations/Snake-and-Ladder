package com.ludo.Snake.and.Ladder.service

import com.ludo.Snake.and.Ladder.Constants
import com.ludo.Snake.and.Ladder.Util.Utilities
import com.ludo.Snake.and.Ladder.model.Board
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.GenericSuccessResponse
import com.ludo.Snake.and.Ladder.model.JoinPlayer
import com.ludo.Snake.and.Ladder.model.MoveRequest
import com.ludo.Snake.and.Ladder.model.MoveResponse
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerBox
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.repository.BoardRepository
import com.ludo.Snake.and.Ladder.repository.GameConfigurationRepository
import com.ludo.Snake.and.Ladder.repository.PlayerRepository
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.vavr.collection.List
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

import java.security.SecureRandom
import java.util.stream.IntStream

@Slf4j
@Service
class PlayerService {

    @Autowired
    PlayerRepository playerRepository

    @Autowired
    BoardRepository boardRepository

    @Autowired
    GameConfigurationRepository gameConfigurationRepository

    static final className = this.class.simpleName

    Either<GenericErrorResponse,Player> playerRegister(PlayerDto playerDto) {
        Random random = new Random()
        List<IntStream> randomNumberGenerator = random.ints(1,0, Integer.MAX_VALUE).findAll()
        String playerId = "player_" + randomNumberGenerator[0].toString()
        Player player = new Player().tap {
            pid = playerId
            name = playerDto.name
            emailId = playerDto.emailId
            password = playerDto.password
        }
        Player playerResponse
        try {
           playerResponse = playerRepository.save(player)
        } catch (Exception ex) {
            log.error("Exception occurred while saving player details", ex)
            return Either.left(new GenericErrorResponse(status: 404, reason: "Exception occurred while saving player details: ${ex.getLocalizedMessage()}"))
        }
        log.info("Player created with playerId: ${player.pid}")
        return Either.right(playerResponse)
    }

    Either<GenericErrorResponse, PlayerBox> joinPlayer(JoinPlayer joinPlayerReq) {
        log.info("[${className}][joinPlayer][Enter]")
        String emailId = joinPlayerReq.emailId
        String gameId = joinPlayerReq.gameId
        Optional<GameConfiguration> OptionalGameConfiguration = gameConfigurationRepository.findById(gameId)
        Optional<Player> OptionalPlayer = playerRepository.findByEmailId(emailId)

        if(OptionalPlayer.isEmpty()) {
            log.error("Player not found with email id: ${emailId}")
            return Either.left(new GenericErrorResponse(status: 404, reason: "EmailId not Found"))
        }
        if(OptionalGameConfiguration.isEmpty()) {
            log.error("gameid ${gameId} not present. No game configuration found.")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Game not Found"))
        }

        GameConfiguration gameConfiguration = OptionalGameConfiguration.get()
        Player player = OptionalPlayer.get()
       // Set<PlayerBox> playerBoxes =

        PlayerBox playerAvailable = gameConfiguration.board.playerBoxes.stream().filter
                                                      {player.pid.equals(it.getPid())}.findFirst().orElse(null)
        if(playerAvailable != null) {
            log.error("Player ${player.pid} already joined game.")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Player Already Joined"))
        }

        if(gameConfiguration.board.playerBoxes.size() == gameConfiguration.playerCount) {
            log.error("Maximum limit reached PlayerCount: ${gameConfiguration.playerCount} ")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Max Player Reached"))
        }

        if(gameConfiguration.gameState != Constants.GameState.JOIN) {
            log.error("Game id ${gameId} is not in Join State")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Game is not in Join State"))
        }

        PlayerBox newPlayerBox = new PlayerBox().tap {
            pid = player.pid
            position = 0
        }

        try {
            Board board = gameConfiguration.board
            board.getPlayerBoxes().add(newPlayerBox)
            board = boardRepository.save(board)
           newPlayerBox = board.playerBoxes.stream().filter
            {player.pid.equals(it.getPid())}.findFirst().orElse(null)
        } catch(Exception ex) {
            log.error("Exception occurred while add player in game. Exception: ${ex.getLocalizedMessage()}")
            return Either.left(new GenericErrorResponse(status: 400, reason: "There was some error while saving, please try again."))
        }
        log.info("[${className}][joinPlayer][Exit]")
        return Either.right(newPlayerBox)
    }

    Either<GenericErrorResponse, GenericSuccessResponse> login(String emailId, String password) {
        log.info("[${className}][login][Enter]")
        Optional<Player> optionalPlayer = playerRepository.findByEmailId(emailId)
        if(optionalPlayer.isEmpty()) {
            log.error("Player not found with email: ${emailId}")
            return Either.left(new GenericErrorResponse(status: 404, reason: "EmailId not found"))
        }
        Player player = optionalPlayer.get()
        if(player.password != password) {
            log.error("Unable to login Password did not match")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Wrong password"))
        }
        log.info("Player successfully validated. Generating Access token")
        log.info("[${className}][login][Exit]")
        Map<String, Object> claims = new HashMap<>()
        claims["name"] = player.name
        claims["emailId"] = player.emailId
        String accessToken = Utilities.generateAccessToken(claims)
        log.info("Access Token = ${accessToken}")
        return Either.right(new GenericSuccessResponse(status: 200, reason: "Login Successfully", accessToken: accessToken))
    }

    Either<GenericErrorResponse, String> movePlayer(MoveRequest moveRequest) {
        log.info("[${className}][nextTurn][Enter]")
        Optional<GameConfiguration> optionalGameConfiguration = gameConfigurationRepository.findById(moveRequest.gameId)
        Optional<Player> optionalPlayer = playerRepository.findByEmailId(moveRequest.emailId)
        if(optionalGameConfiguration.isEmpty()) {
            log.error("Game not found for gamaId ${moveRequest.gameId}")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Game Not Found"))
        }

        if(optionalPlayer.isEmpty()) {
            log.error("Player Not found for emailId ${moveRequest.emailId}")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Player Not Found"))
        }
        Player player = optionalPlayer.get()
        GameConfiguration gameConfiguration = optionalGameConfiguration.get()
        if(gameConfiguration.gameState == Constants.GameState.FINISHED) {
            log.info("Game for game id ${gameConfiguration.id} is already finished.")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Game already Finished"))
        }

        PlayerBox currentPlayer = gameConfiguration.board.playerBoxes.stream().filter
                                      {player.pid.equals(it.getPid())}.findAny().orElse(null)

        MoveResponse moveResponse = new MoveResponse()
        moveResponse.oldPosition = currentPlayer.getPosition()

        currentPlayer.position = currentPlayer.position + moveRequest.sum
        String newSnakeOrLadderPosition = gameConfiguration.board.snakeOrLadder.get(currentPlayer.position.toString())

        if(newSnakeOrLadderPosition != null) {
            currentPlayer.position = newSnakeOrLadderPosition.toInteger()
        }

        moveResponse.newPosition = currentPlayer.position
        moveResponse.emailId = moveRequest.emailId

        if(gameConfiguration.boardSize == currentPlayer.position) {
            moveResponse.gameFinished = true
            gameConfiguration.gameState = Constants.GameState.FINISHED
        }

        try {
            gameConfiguration.board.playerBoxes.add(currentPlayer)
            gameConfigurationRepository.save(gameConfiguration)
        } catch (Exception ex){
            log.error("Error while saving game configuration ${ex.getLocalizedMessage()}")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Error occurred while saving"))
        }

        log.info("[${className}][nextTurn][Exit]")
        return Either.right(moveResponse)
    }

}
