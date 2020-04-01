package com.example.springdataautomappingdemo.services;

import com.example.springdataautomappingdemo.domain.dtos.AddGameDto;
import com.example.springdataautomappingdemo.domain.dtos.EditGameDto;
import com.example.springdataautomappingdemo.domain.entities.Game;

public interface GameService {
    void addGame(AddGameDto addGameDto);

    void editGame(EditGameDto editGameDto);

    void deleteGame(Long id);

    void getAllGames();

    void getGameInfoByName(String title);

    Game getGameByTitle(String title);
}
