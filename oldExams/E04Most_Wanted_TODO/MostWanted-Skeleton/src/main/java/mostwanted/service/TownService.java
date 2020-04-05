package mostwanted.service;

import mostwanted.domain.entities.Town;

import java.io.IOException;

public interface TownService {

    Boolean townsAreImported();

    String readTownsJsonFile() throws IOException;

    String importTowns(String townsFileContent);

    String exportRacingTowns();

    Town findTownByName(String name);
}
