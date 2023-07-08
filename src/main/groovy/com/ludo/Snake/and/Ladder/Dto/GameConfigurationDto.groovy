package com.ludo.Snake.and.Ladder.Dto

import com.ludo.Snake.and.Ladder.Constants.GameState

class GameConfigurationDto {
    Integer boardRows
    Integer boardColumns
    Integer diceCount
    Integer playerCount
    String emailId
    GameState gameState
    Map<String,String> snakeOrLadder
}
