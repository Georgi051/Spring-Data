package com.example.springdataautomappingdemo.services;

import com.example.springdataautomappingdemo.domain.dtos.AddGameDto;
import com.example.springdataautomappingdemo.domain.dtos.EditGameDto;
import com.example.springdataautomappingdemo.domain.entities.Game;
import com.example.springdataautomappingdemo.repositories.GameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addGame(AddGameDto addGameDto) {
       try {
            Game game = this.modelMapper.map(addGameDto, Game.class);
            this.gameRepository.saveAndFlush(game);
            System.out.printf("Game %s is added successfully%n", game.getTitle());
        } catch (Exception e) {
            System.out.printf("Game %s already exist!%n", addGameDto.getTitle());
        }
    }

    @Override
    public void editGame(EditGameDto editGameDto) {
        Game game = this.gameRepository.findById(editGameDto.getId()).orElse(null);

        if (game == null) {
            System.out.printf("Game with id= %d does not exist!!!%n", editGameDto.getId());
        } else {
            editNewGameValues(editGameDto, game);
            this.gameRepository.saveAndFlush(game);
            System.out.printf("You have edited a game with id= %d%n", game.getId());
        }
    }

    @Override
    public void deleteGame(Long id) {
        Game game = this.gameRepository.findById(id).orElse(null);
        if (game != null){
            this.gameRepository.delete(game);
            System.out.printf("Game with id= %d is deleted%n",id);
        }else {
            System.out.printf("Game with id= %d does not exist!!!%n",id);
        }
    }

    @Override
    public void getAllGames() {
        if (this.gameRepository.count() == 0){
            System.out.println("No games to show");
        }
        this.gameRepository.findAll().forEach(g -> System.out.printf("%s %.2f%n",g.getTitle(),g.getPrice()));
    }

    @Override
    public void getGameInfoByName(String title) {
        Game game = this.gameRepository.findGameByTitle(title);
        if (game != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String date = formatter.format(game.getRealiseDate());
            System.out.printf("Title: %s%n" +
                    "Price: %.2f%n" +
                    "Description: %s%n" +
                    "Release date: %s%n",game.getTitle(),game.getPrice(),game.getDescription(),
                   date);
        }else {
            System.out.printf("%s game does not exist!!!%n",title);
        }
    }

    @Override
    public Game getGameByTitle(String title) {
      return this.gameRepository.findGameByTitle(title);
    }

    private void editNewGameValues(EditGameDto editGameDto, Game game) {
        if (editGameDto.getTrailer() != null) {
            game.setTrailer(editGameDto.getTrailer());
        }
        if (editGameDto.getSize() != 0.0) {
            game.setSize(editGameDto.getSize());
        }
        if (editGameDto.getPrice() != null) {
            game.setPrice(editGameDto.getPrice());
        }
        if (editGameDto.getDescription() != null) {
            game.setDescription(editGameDto.getDescription());
        }
        if (editGameDto.getImage() != null) {
            game.setImage(editGameDto.getImage());
        }
        if (editGameDto.getRealiseDate() != null) {
            game.setRealiseDate(editGameDto.getRealiseDate());
        }
        if (editGameDto.getTitle() != null) {
            game.setTitle(editGameDto.getTitle());
        }
    }
}
