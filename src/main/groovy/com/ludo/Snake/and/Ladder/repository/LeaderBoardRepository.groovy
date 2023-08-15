package com.ludo.Snake.and.Ladder.repository

import com.ludo.Snake.and.Ladder.model.LeaderBoard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Integer>{
        List<LeaderBoard> findAllByOrderByGlobalRankingDesc()
}
