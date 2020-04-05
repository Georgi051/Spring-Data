package mostwanted.service;

import mostwanted.domain.dtos.races.RaceImportDto;
import mostwanted.domain.dtos.races.RaceImportRootDto;
import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Race;
import mostwanted.domain.entities.RaceEntry;
import mostwanted.repository.RaceRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import mostwanted.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static mostwanted.common.Constants.INCORRECT_DATA_MESSAGE;

@Service
public class RaceServiceImpl implements RaceService {

    private final static String RACES_XML_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/races.xml";

    private final RaceRepository raceRepository;
    private final ModelMapper modelMapper;
    private final DistrictService districtService;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final XmlParser xmlParser;
    private final RaceEntryService raceEntryService;

    @Autowired
    public RaceServiceImpl(RaceRepository raceRepository, ModelMapper modelMapper, DistrictService districtService, ValidationUtil validationUtil, FileUtil fileUtil, XmlParser xmlParser, RaceEntryService raceEntryService) {
        this.raceRepository = raceRepository;
        this.modelMapper = modelMapper;
        this.districtService = districtService;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.xmlParser = xmlParser;
        this.raceEntryService = raceEntryService;
    }


    @Override
    public Boolean racesAreImported() {
        return this.raceRepository.count() > 0;
    }

    @Override
    public String readRacesXmlFile() throws IOException {
        return this.fileUtil.readFile(RACES_XML_FILE_PATH);
    }

    @Override
    public String importRaces() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        RaceImportRootDto raceImportRootDto = this.xmlParser
                .unmarshalFromFile(RACES_XML_FILE_PATH,RaceImportRootDto.class);

        for (RaceImportDto rDto : raceImportRootDto.getRaceImportDtos()) {
            if (this.validationUtil.isValid(rDto)){
                District district = this.districtService.findByName(rDto.getDistrictName());

                Set<RaceEntry> raceEntries = rDto.getEntryImportRootDto().getEntityList()
                        .stream()
                        .map(entry -> this.raceEntryService.findOneById(entry.getId()))
                        .collect(Collectors.toSet());

                if (district == null || raceEntries.contains(null)){
                    sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                }else {
                    Race race = this.modelMapper.map(rDto,Race.class);
                    race.setDistrict(district);
                    race.setEntries(raceEntries);
                    this.raceRepository.saveAndFlush(race);
                    raceEntries.forEach(raceEntry -> raceEntry.setRace(race));
                }
            }else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}