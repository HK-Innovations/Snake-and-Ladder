package com.ludo.Snake.and.Ladder.model

import com.ludo.Snake.and.Ladder.Constants

class GameConfigurationDto {
    Integer boardRows
    Integer boardColumns
    Integer diceCount
    Integer playerCount
    Constants.GameState gameState
    Map<String,String> snakeOrLadder
}
