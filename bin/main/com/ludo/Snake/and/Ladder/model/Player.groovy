package com.ludo.Snake.and.Ladder.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Player {

    @Id
    String pid

    @NotNull
    String name

    @NotNull
    @Column(unique = true)
    String emailId

    @NotNull
    String password
}
