package com.ludo.Snake.and.Ladder.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Column

class PlayerDto {
    @NotNull
    String name

    @NotNull
    @Column(unique = true)
    String emailId

    @NotNull
    String password
}
