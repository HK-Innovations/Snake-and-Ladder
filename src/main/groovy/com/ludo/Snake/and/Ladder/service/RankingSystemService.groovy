package com.ludo.Snake.and.Ladder.service

import com.ludo.Snake.and.Ladder.model.Career
import com.ludo.Snake.and.Ladder.model.GameConfiguration
import com.ludo.Snake.and.Ladder.model.GenericErrorResponse
import com.ludo.Snake.and.Ladder.model.LeaderBoard
import com.ludo.Snake.and.Ladder.model.Player
import com.ludo.Snake.and.Ladder.model.PlayerBox
import com.ludo.Snake.and.Ladder.repository.CareerRepository
import com.ludo.Snake.and.Ladder.repository.LeaderBoardRepository
import com.ludo.Snake.and.Ladder.repository.PlayerRepository
import groovy.util.logging.Slf4j
import io.vavr.control.Either
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class RankingSystemService {
    private final className = this.class.simpleName

    @Autowired
    PlayerRepository playerRepository

    @Autowired
    CareerRepository careerRepository

    @Autowired
    LeaderBoardRepository leaderBoardRepository

    def updateRanks(GameConfiguration gameConfiguration, PlayerBox winnerPlayerBox) {
        log.info("[$className][upadteRanks][Enter]")
        HashSet<PlayerBox> playerBoxList = gameConfiguration.board.playerBoxes

        for(PlayerBox playerBox: playerBoxList) {
            Player player = playerRepository.findById(playerBox.pid).get()
            Career career = player.career
            LeaderBoard leaderBoard = player.leaderBoard
            career.totalGamesPlayed = career.totalGamesPlayed + 1
            if(player.pid == winnerPlayerBox.pid) {
                //winner player
                career.totalWins = career.totalWins + 1
            } else {
                career.totalLoss = career.totalLoss + 1
            }
            leaderBoard.globalRanking = career.totalWins

            try{
                careerRepository.save(career)
            } catch (Exception ex) {
                log.error("Exception occurred while saving career", ex)
            }

            try {
                leaderBoardRepository.save(leaderBoard)
            } catch (Exception ex) {
                log.error("Exception generated while saving leaderBoard", ex)
            }
        }
        log.info("[$className][upadteRanks][Exit]")
    }

    Either<GenericErrorResponse, List<LeaderBoard>> getLeaderBoard() {
        log.info("[$className][getLeaderBoard][Enter]")
        List<LeaderBoard> leaderBoardList
        try {
            leaderBoardList = leaderBoardRepository.findAllByOrderByGlobalRankingDesc()
        } catch (Exception ex) {
            log.error("Exception occurred while getting Leaderboard: ", ex)
            return Either.left(new GenericErrorResponse().tap {
                status = 400
                reason = "Exception occurred while getting leaderboard"
            })
        }

        log.info("[$className][getLeaderBoard][Exit]")
        return Either.right(leaderBoardList)
    }

    Either<GenericErrorResponse, Career> getPlayerCareer(String emailId) {
        log.info("[$className][getPlayerCareer][Enter]")
        Player player
        try{
            player = playerRepository.findByEmailId(emailId).get()
        } catch (Exception ex) {
            log.error("Exception occurred while getting career: ", ex)
            return Either.left(new GenericErrorResponse().tap {
                status = 400
                reason = "Exception occurred while getting career"
            })
        }
        log.info("[$className][getPlayerCareer][Exit]")
        return Either.right(player.career)
    }
}
