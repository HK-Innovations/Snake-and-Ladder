package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne


@Entity
class PlayerBox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id
    String pid
    Integer position

//    Integer jumpStart
//    Integer jumpEnd
//    String playerId
//    @Convert(converter = GenericListConverter<Player>.class)
//    List<Player> activePlayers

//    PlayerBox(Integer jumpStart) {
//        this.position = jumpStart
//    }
}
