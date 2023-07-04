package com.ludo.Snake.and.Ladder.repository

import com.ludo.Snake.and.Ladder.model.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository extends JpaRepository<Board, String>{

}