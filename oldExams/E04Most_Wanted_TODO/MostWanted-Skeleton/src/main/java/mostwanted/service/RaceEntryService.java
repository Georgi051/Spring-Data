package mostwanted.service;

import mostwanted.domain.entities.Race;
import mostwanted.domain.entities.RaceEntry;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface RaceEntryService {

    Boolean raceEntriesAreImported();

    String readRaceEntriesXmlFile() throws IOException;

    String importRaceEntries() throws JAXBException, IOException;

    RaceEntry findOneById(Integer id);
}
