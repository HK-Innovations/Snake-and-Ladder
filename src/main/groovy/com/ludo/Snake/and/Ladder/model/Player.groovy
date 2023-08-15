package com.ludo.Snake.and.Ladder.model

import groovyjarjarantlr4.v4.runtime.misc.NotNull
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Career career

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    LeaderBoard leaderBoard
}
