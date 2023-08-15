package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id
    Integer totalGamesPlayed
    Integer totalWins
    Integer totalLoss
}
