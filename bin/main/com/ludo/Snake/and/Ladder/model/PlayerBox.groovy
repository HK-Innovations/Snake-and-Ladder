package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class PlayerBox {

    @Id
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
