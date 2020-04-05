package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.RacerImportDto;
import mostwanted.domain.entities.Racer;
import mostwanted.domain.entities.Town;
import mostwanted.repository.RacerRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static mostwanted.common.Constants.*;

@Service
public class RacerServiceImpl implements RacerService {

    private final static String RACERS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/racers.json";

    private final RacerRepository racerRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;

    @Autowired
    public RacerServiceImpl(RacerRepository racerRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, Gson gson) {
        this.racerRepository = racerRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }


    @Override
    public Boolean racersAreImported() {
        return this.racerRepository.count() > 0;
    }

    @Override
    public String readRacersJsonFile() throws IOException {
        return this.fileUtil.readFile(RACERS_JSON_FILE_PATH);
    }

    @Override
    public String importRacers(String racersFileContent) {
        StringBuilder sb = new StringBuilder();
        RacerImportDto[] racerImportDtos = this.gson.fromJson(racersFileContent, RacerImportDto[].class);

        for (RacerImportDto racerImportDto : racerImportDtos) {
            if (this.validationUtil.isValid(racerImportDto)) {
                if (this.racerRepository.findByName(racerImportDto.getName()) == null) {
                    Town town = this.townService.findTownByName(racerImportDto.getTownName());

                    if (town == null) {
                        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                    } else {
                        Racer racer = this.modelMapper.map(racerImportDto, Racer.class);
                        racer.setTown(town);
                        this.racerRepository.saveAndFlush(racer);
                        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE, "Racer", racer.getName()))
                                .append(System.lineSeparator());
                    }
                } else {
                    sb.append(DUPLICATE_DATA_MESSAGE).append(System.lineSeparator());
                }
            } else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String exportRacingCars() {
        StringBuilder sb = new StringBuilder();
        List<Racer> racers = this.racerRepository.findAll();

        racers.stream().sorted((f, s) -> {
            int i = s.getCars().size() - f.getCars().size();
            if (i == 0) {
                i = f.getName().compareTo(s.getName());
            }
            return i;
        }).forEach(racer -> {
            if (racer.getAge() == null) {
                sb.append(String.format("Name: %s%nCars:%n", racer.getName()));
                racer.getCars()
                        .forEach(c -> sb.append(String.format(" %s %s %s%n",
                                c.getBrand(), c.getModel(), c.getYearOfProduction())));
            } else {
                sb.append(String.format("Name: %s %d%nCars:%n", racer.getName(), racer.getAge()));
                racer.getCars()
                        .forEach(c -> sb.append(String.format(" %s %s %s%n"
                                , c.getBrand(), c.getModel(), c.getYearOfProduction())));
            }
        });

        return sb.toString().trim();
    }

    @Override
    public Racer findByName(String name) {
        return this.racerRepository.findByName(name);
    }
}
