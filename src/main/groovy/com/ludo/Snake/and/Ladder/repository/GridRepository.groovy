package com.ludo.Snake.and.Ladder.repository


import com.ludo.Snake.and.Ladder.model.PlayerBox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GridRepository extends JpaRepository<PlayerBox, String>{

}