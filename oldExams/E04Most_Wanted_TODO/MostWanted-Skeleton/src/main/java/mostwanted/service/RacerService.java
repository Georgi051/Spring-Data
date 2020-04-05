package mostwanted.service;

import mostwanted.domain.entities.Racer;

import java.io.IOException;

public interface RacerService {

    Boolean racersAreImported();

    String readRacersJsonFile() throws IOException;

    String importRacers(String racersFileContent);

    String exportRacingCars();

    Racer findByName(String name);
}
