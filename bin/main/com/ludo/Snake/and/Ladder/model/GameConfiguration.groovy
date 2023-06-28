package com.ludo.Snake.and.Ladder.model

import com.ludo.Snake.and.Ladder.Constants
import jakarta.persistence.CascadeType
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import lombok.NoArgsConstructor


@Entity
@NoArgsConstructor
class GameConfiguration {

    @Id
    String id

    Integer boardRows
    Integer boardColumns
    Integer boardSize
    Integer playerCount
   Constants.GameState gameState
    //@Convert(converter = GenericListConverter<Board>.class)
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    Board board
//    Set<String> snakeOrLadder
//    @Convert(converter = GenericListConverter<Player>.class)
//    List<Player> players = new ArrayList<>()
//    @Convert(converter = GenericListConverter<PlayerBox>.class)
//    List<PlayerBox> gridWithJump = new ArrayList<>()

//    GameConfiguration(Integer boardRows, Integer boardColumns) {
//        this.boardSize = boardRows * boardColumns
//    }

    GameConfiguration() {

    }

    Map asJson() {
        Map json = [:]
        json["board"] = board.asJson()
    }
}
