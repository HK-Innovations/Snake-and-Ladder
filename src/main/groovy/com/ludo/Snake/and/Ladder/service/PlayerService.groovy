package com.ludo.Snake.and.Ladder.service

import com.ludo.Snake.and.Ladder.Constants
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerBox
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.repository.GameConfigurationRepository
import com.ludo.Snake.and.Ladder.repository.PlayerRepository
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
            log.info("Exception occurred while saving player details", ex)
            return Either.left(new GenericErrorResponse(status: 404, reason: "Exception occurred while saving player details: ${ex.getLocalizedMessage()}"))
        }
        log.info("Player created with playerId: ${player.pid}")
        return Either.right(playerResponse)
    }

    def joinPlayer(String gameId, String playerId, String emailId) {
        log.info("[${className}][joinPlayer][Enter]")
        Optional<GameConfiguration> gameConfiguration = gameConfigurationRepository.findByIdAndGameState(gameId, Constants.GameState.JOIN)
        Optional<Player> player = playerRepository.findById(playerId)
        if(player.isEmpty()) {
            player = playerRepository.findByEmailId(emailId)
        }
        if(gameConfiguration.isEmpty()) {
            log.info("[${className}][joinPlayer][Exit]")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Game not Found"))
        }
        if(player.isEmpty()){
            log.info("[${className}][joinPlayer][Exit]")
            return Either.left(new GenericErrorResponse(status: 404, reason: "Player Not Found"))
        }
        Optional<PlayerBox> playerBox = gameConfiguration.get().board.playerBoxes.findById(playerId)
        if(!playerBox.isEmpty()) {
            log.info("[${className}][joinPlayer][Exit]")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Player Already Joined"))
        }

        PlayerBox newPlayerBox = new PlayerBox().tap {
            pid = playerId
            position = 0
        }
        try {
            gameConfiguration.get().board.playerBoxes.add(newPlayerBox)
        } catch(Exception ex) {
            log.info("[${className}][joinPlayer][Exit]")
            return Either.left(new GenericErrorResponse(status: 400, reason: "Player Not Added"))
        }

        log.info("[${className}][joinPlayer][Exit]")
        return Either.right(newPlayerBox)
    }
}
