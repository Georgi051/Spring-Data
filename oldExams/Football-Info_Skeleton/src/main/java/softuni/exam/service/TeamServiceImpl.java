package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.teamsSeed.TeamRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static softuni.exam.constants.GlobalConstants.TEAMS_SEED;
import static softuni.exam.constants.GlobalConstants.VALID_DATA;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final PictureRepository pictureRepository;


    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, PictureRepository pictureRepository) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public String importTeams() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        TeamRootDto tDto = this.xmlParser.unmarshalFromFile(TEAMS_SEED, TeamRootDto.class);
        tDto.getTeamDtoList().forEach(teamDto -> {
            if (this.validationUtil.isValid(teamDto)) {
                if (this.teamRepository.findByName(teamDto.getName()) == null) {
                    Team team = this.modelMapper.map(teamDto, Team.class);

                    Picture picture = this.pictureRepository.findByUrl(teamDto.getPicture().getUrl());
                    if (picture != null){
                        team.setPicture(picture);
                        this.teamRepository.saveAndFlush(team);
                        sb.append(String.format("Successfully imported - %s",team.getName()))
                                .append(System.lineSeparator());
                    }else {
                        sb.append("Invalid team").append(System.lineSeparator());
                    }

                } else {
                    sb.append("This team is already in data base!")
                            .append(System.lineSeparator());
                }
            } else {
                sb.append("Invalid team").append(System.lineSeparator());
            }
        });

        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {
        return Files.readString(Paths.get(TEAMS_SEED));
    }

    @Override
    public Team findByName(String name) {
        return this.teamRepository.findByName(name);
    }
}
