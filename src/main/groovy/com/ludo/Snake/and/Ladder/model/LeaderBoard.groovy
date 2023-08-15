package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class LeaderBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    String emailId

    Double globalRanking
}
