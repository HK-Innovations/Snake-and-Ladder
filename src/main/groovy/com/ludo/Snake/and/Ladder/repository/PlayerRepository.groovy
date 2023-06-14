package com.ludo.Snake.and.Ladder.repository

import com.ludo.Snake.and.Ladder.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository extends JpaRepository<Player, String>{

}