package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
class Board {
    @Id
    String id

    //@Convert(converter = GenericListConverter<Dice>.class)
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    Dice dice

    //@Convert(converter = GenericListConverter<Player>.class)
    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    Set<PlayerBox> playerBoxes = new HashSet<>()

    @Convert(converter = GenericMapConverter<String, String>.class)
    Map<String,String> snakeOrLadder

    Integer playerTurn

    Board() {
       // playerDeque = new ArrayList<>()
    }

//    Map asJson() {
//        Map json = [:]
//    }
}
