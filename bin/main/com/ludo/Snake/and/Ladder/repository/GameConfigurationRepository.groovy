package com.ludo.Snake.and.Ladder.repository

import com.ludo.Snake.and.Ladder.model.GameConfiguration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameConfigurationRepository extends JpaRepository<GameConfiguration,String>{

}