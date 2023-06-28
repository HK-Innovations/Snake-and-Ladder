package com.ludo.Snake.and.Ladder.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Dice {
    @Id
    String id

    Integer count
}
