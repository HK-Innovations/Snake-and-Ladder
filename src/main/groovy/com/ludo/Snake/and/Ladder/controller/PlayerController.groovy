package com.ludo.Snake.and.Ladder.controller

import com.ludo.Snake.and.Ladder.Dto.AccessTokenResponse

import com.ludo.Snake.and.Ladder.Dto.PlayerBoxResponse
import com.ludo.Snake.and.Ladder.model.Career
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.JoinPlayer
import com.ludo.Snake.and.Ladder.model.LeaderBoard
import com.ludo.Snake.and.Ladder.model.MoveRequest
import com.ludo.Snake.and.Ladder.model.MoveResponse
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerDto
import com.ludo.Snake.and.Ladder.service.PlayerService
import com.ludo.Snake.and.Ladder.service.RankingSystemService
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
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

    @Autowired
    RankingSystemService rankingSystemService

    private final className = this.class.simpleName

    @PostMapping("/register")
    def registerPlayer(@RequestBody @Validated PlayerDto playerDto) {
        log.info("[${className}][registerPlayer][Enter]")
        Either<GenericErrorResponse, Player> playerResponse = playerService.playerRegister(playerDto)
        if(playerResponse.isLeft()){
            log.info("[${className}][registerPlayer][Exit]")
            return new ResponseEntity<>(playerResponse.getLeft(), HttpStatusCode.valueOf(playerResponse.getLeft().status))
        }
        log.info("[${className}][registerPlayer][Exit]")
        return playerResponse.get()
    }

    @MessageMapping("/joinPlayer") // /app/joinPlayer (message part)
    @SendTo("/joinPlayerAll/public") // subscription part
    @PostMapping("/join")
    def joinPlayer(@Payload JoinPlayer joinPlayerReq) {
        log.info("[${className}][joinPlayer][Enter]")
        Either<GenericErrorResponse, PlayerBoxResponse> joinResponse = playerService.joinPlayer(joinPlayerReq)
        if(joinResponse.isLeft()) {
            log.info("[${className}][joinPlayer][Exit]")
            return new ResponseEntity<>(joinResponse.getLeft(), HttpStatusCode.valueOf(joinResponse.getLeft().status))
        }

        log.info("[${className}][joinPlayer][Exit]")
        return joinResponse.get()
    }

    @PostMapping("/login")
    def login(@RequestBody Map<String, Object> request) {
        log.info("[${className}][login][Enter]")
        String emailId = request.emailId
        String password = request.password
        Either<GenericErrorResponse, AccessTokenResponse> loginResponse = playerService.login(emailId,password)
        if(loginResponse.isLeft()) {
            log.info("[${className}][login][Exit]")
            return new ResponseEntity<>(loginResponse.getLeft(), HttpStatusCode.valueOf(loginResponse.getLeft().status))
        }

        log.info("[${className}][login][Exit]")
        return loginResponse.get()
    }

    @MessageMapping("/movePlayer")  // /app/movePlayer
    @SendTo("/movePlayerAll/public") // subscription part
    @PostMapping("/movePlayer")
    def movePlayer(@Payload @RequestBody MoveRequest moveRequest) {
        log.info("[${className}][movePlayer][Enter]")
        Either<GenericErrorResponse, MoveResponse> response = playerService.movePlayer(moveRequest)
        if(response.isLeft()) {
            log.info("[${className}][movePlayer][Exit]")
            return new ResponseEntity<>(response.getLeft(), HttpStatusCode.valueOf(response.getLeft().status))
        }
        log.info("[${className}][movePlayer][Exit]")
        return response.get()
    }

    @GetMapping("/leaderBoard")
    def getLeaderBoard() {
        log.info("[$className][getLeaderBoard][Enter]")
        Either<GenericErrorResponse, List<LeaderBoard>> response = rankingSystemService.getLeaderBoard()
        if(response.isLeft()) {
            log.info("[${className}][getLeaderBoard][Exit]")
            return new ResponseEntity<>(response.getLeft(), HttpStatusCode.valueOf(response.getLeft().status))
        }
        log.info("[$className][getLeaderBoard][Exit]")
        return response.get()
    }

    @GetMapping("/career")
    def getPlayerCareer(@RequestBody Map<String, Object> request) {
        log.info("[$className][getPlayerCareer][Enter]")
        Either<GenericErrorResponse, Career> response = rankingSystemService.getPlayerCareer(request.emailId)
        if(response.isLeft()) {
            log.info("[${className}][getLeaderBoard][Exit]")
            return new ResponseEntity<>(response.getLeft(), HttpStatusCode.valueOf(response.getLeft().status))
        }
        log.info("[$className][getPlayerCareer][Exit]")
        return response.get()
    }
}
