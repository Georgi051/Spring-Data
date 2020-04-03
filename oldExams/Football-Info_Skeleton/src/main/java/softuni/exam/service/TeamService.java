package softuni.exam.service;

import softuni.exam.domain.entities.Team;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface TeamService {

    String importTeams() throws IOException, JAXBException;

    boolean areImported();

    String readTeamsXmlFile() throws IOException;

    Team findByName(String name);
}
