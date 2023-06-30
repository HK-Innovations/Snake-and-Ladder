package com.ludo.Snake.and.Ladder.service

import com.ludo.Snake.and.Ladder.Constants
import com.ludo.Snake.and.Ladder.Util.Utilities
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.GenericSuccessResponse
import com.ludo.Snake.and.Ladder.model.JoinPlayer
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerBox
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.repository.GameConfigurationRepository
import com.ludo.Snake.and.Ladder.repository.PlayerRepository
import groovy.util.logging.Slf4j
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.security.SecureRandom
import java.util.stream.IntStream

@Slf4j
@Service
class PlayerService {

    @Autowired
    PlayerRepository playerRepository

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
        List<PlayerBox> playerBoxes = gameConfiguration.board.playerBoxes

        boolean playerAvailable = playerBoxes.stream().filter {player.pid.equals(it.getPid())}.findAny().orElse(null)
        if(playerAvailable) {
            log.error("Player ${player.pid} already joined game.")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Player Already Joined"))
        }

        if(playerBoxes.size() == gameConfiguration.playerCount) {
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
            gameConfiguration.board.playerBoxes.add(newPlayerBox)
            gameConfigurationRepository.save(gameConfiguration)
        } catch(Exception ex) {
            log.error("Exception occurred while add player in game. Exception: ${ex.getLocalizedMessage()}")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Player Not Added"))
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

}
