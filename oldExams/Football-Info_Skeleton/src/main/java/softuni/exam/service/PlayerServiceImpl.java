package softuni.exam.service;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.PlayerSeedDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.PLAYERS_SEED;

@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final TeamService teamService;
    private final PictureService pictureService;



    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, TeamService teamService, PictureService pictureService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.teamService = teamService;
        this.pictureService = pictureService;
    }

    @Override
    public String importPlayers() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        PlayerSeedDto[] pDto = this.gson.fromJson(new FileReader(PLAYERS_SEED),PlayerSeedDto[].class);

        System.out.println();
        for (PlayerSeedDto playerSeedDto : pDto) {
            if (this.validationUtil.isValid(playerSeedDto)){
                if (this.playerRepository.findByFirstNameAndLastName
                        (playerSeedDto.getFirstName(),playerSeedDto.getLastName()) == null){
                   if (playerSeedDto.getPosition() != null){
                       Player player = this.modelMapper.map(playerSeedDto,Player.class);
                       Team team = this.teamService.findByName(playerSeedDto.getTeam().getName());
                       Picture picture = this.pictureService.findByUrl(playerSeedDto.getPicture().getUrl());
                       player.setTeam(team);
                       player.setPicture(picture);
                       this.playerRepository.saveAndFlush(player);
                       sb.append(String.format("Successfully imported player: %s %s"
                               ,player.getFirstName()
                               ,player.getLastName()))
                               .append(System.lineSeparator());
                   }else {
                       sb.append("Invalid player").append(System.lineSeparator());
                   }
                }else {
                    sb.append("This player is already in data base!")
                            .append(System.lineSeparator());
                }
            }else {
                sb.append("Invalid player").append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        return Files.readString(Paths.get(PLAYERS_SEED));
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
       StringBuilder sb = new StringBuilder();
       this.playerRepository.getAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal.valueOf(100000))
               .forEach(player -> {
                   sb.append(String.format("Player name: %s %s",player.getFirstName(),player.getLastName()))
                           .append(System.lineSeparator())
                           .append(String.format("Number: %d",player.getNumber()))
                           .append(System.lineSeparator())
                           .append(String.format("Salary: %s",player.getSalary()))
                           .append(System.lineSeparator())
                           .append(String.format("Team: %s",player.getTeam().getName()))
                           .append(System.lineSeparator());
               });
        return sb.toString();
    }

    @Override
    public String exportPlayersInATeam() {
        StringBuilder sb = new StringBuilder();

        this.playerRepository.findAllByTeamName("North Hub").forEach(p ->{
            sb.append(String.format("Player name: %s %s - %s",p.getFirstName(),p.getLastName(),p.getPosition()))
                    .append(System.lineSeparator())
                    .append(String.format("Number: %d",p.getNumber()))
                    .append(System.lineSeparator());
        });
        return sb.toString();
    }
}
