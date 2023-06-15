package com.ludo.Snake.and.Ladder.controller

import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.JoinPlayer
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerBox
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.service.PlayerService
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import jakarta.annotation.Nullable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/player")
@Slf4j
class PlayerController {

    @Autowired
    PlayerService playerService

    private final className = this.class.simpleName

    @PostMapping("/register")
    def registerPlayer(@RequestBody @Validated PlayerDto playerDto) {
        log.info("[${className}][registerPlayer][Enter]")
        Either<GenericErrorResponse, Player> playerResponse = playerService.playerRegister(playerDto)
        if(playerResponse.isLeft()){
            log.info("[${className}][registerPlayer][Exit]")
            return playerResponse.getLeft()
        }
        log.info("[${className}][registerPlayer][Exit]")
        return playerResponse.get()
    }

    @PostMapping("/join")
    def joinPlayer(@RequestBody JoinPlayer joinPlayerReq) {
        log.info("[${className}][joinPlayer][Enter]")
        Either<GenericErrorResponse, PlayerBox> joinResponse = playerService.joinPlayer(joinPlayerReq)
        if(joinResponse.isLeft()) {
            log.info("[${className}][joinPlayer][Exit]")
            return joinResponse.getLeft()
        }

        log.info("[${className}][joinPlayer][Exit]")
        return joinResponse.get()
    }
}
