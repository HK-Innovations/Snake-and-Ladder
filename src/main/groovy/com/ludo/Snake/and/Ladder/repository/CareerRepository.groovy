package com.ludo.Snake.and.Ladder.repository

import com.ludo.Snake.and.Ladder.model.Career
import org.springframework.data.jpa.repository.JpaRepository

interface CareerRepository extends  JpaRepository<Career, Integer>{


}