package com.example.springdataautomappingdemo.repositories;


import com.example.springdataautomappingdemo.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game ,Long> {
    Game findGameByTitle(String title);
}
