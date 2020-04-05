package mostwanted.service;

import mostwanted.domain.dtos.raceentries.RaceEntryImportDto;
import mostwanted.domain.dtos.raceentries.RaceEntryImportRootDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Race;
import mostwanted.domain.entities.RaceEntry;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.RaceEntryRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static mostwanted.common.Constants.INCORRECT_DATA_MESSAGE;
import static mostwanted.common.Constants.SUCCESSFUL_IMPORT_MESSAGE;

@Service
@Transactional
public class RaceEntryServiceImpl implements RaceEntryService {

    private final static String RACE_ENTRIES_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/race-entries.xml";

    private final RaceEntryRepository raceEntryRepository;
    private final CarService carService;
    private final RacerService racerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;

    @Autowired
    public RaceEntryServiceImpl(RaceEntryRepository raceEntryRepository, CarService carService, RacerService racerService, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, XmlParser xmlParser) {
        this.raceEntryRepository = raceEntryRepository;
        this.carService = carService;
        this.racerService = racerService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public Boolean raceEntriesAreImported() {
        return this.raceEntryRepository.count() > 0;
    }

    @Override
    public String readRaceEntriesXmlFile() throws IOException {
        return this.fileUtil.readFile(RACE_ENTRIES_XML_FILE_PATH);
    }

    @Override
    public String importRaceEntries() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        RaceEntryImportRootDto rootDtos = this.xmlParser.unmarshalFromFile(RACE_ENTRIES_XML_FILE_PATH,RaceEntryImportRootDto.class);

        List<RaceEntryImportDto> raceList = rootDtos.getRaceList();
        for (RaceEntryImportDto raceEntryDto : raceList) {
            if (this.validationUtil.isValid(raceEntryDto)) {
                Car car = this.carService.findById(raceEntryDto.getId());
                Racer racer = this.racerService.findByName(raceEntryDto.getRacer());

                if (racer == null || car == null) {
                    sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                } else {
                    RaceEntry raceEntry = new RaceEntry();
                    raceEntry.setFinishTime(raceEntryDto.getFinishTime());
                    raceEntry.setHasFinished(raceEntryDto.isHasFinished());
                    raceEntry.setCar(car);
                    raceEntry.setRacer(racer);
                    this.raceEntryRepository.saveAndFlush(raceEntry);
                    sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "RaceEntry", raceEntry.getId()))
                            .append(System.lineSeparator());
                }
            } else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public RaceEntry findOneById(Integer id) {
        return this.raceEntryRepository.findById(id).orElse(null);
    }
}
