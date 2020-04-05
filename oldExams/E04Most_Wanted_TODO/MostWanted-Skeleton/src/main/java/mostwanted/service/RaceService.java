package mostwanted.service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface RaceService {

    Boolean racesAreImported();

    String readRacesXmlFile() throws IOException;

    String importRaces() throws JAXBException, IOException;
}
