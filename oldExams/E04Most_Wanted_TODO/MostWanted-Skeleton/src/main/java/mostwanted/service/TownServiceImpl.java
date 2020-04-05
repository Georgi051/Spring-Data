package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.TownImportDto;
import mostwanted.domain.entities.Town;
import mostwanted.repository.TownRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static mostwanted.common.Constants.*;

@Service
public class TownServiceImpl implements TownService{

    private final static String TOWNS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/towns.json";

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }

    @Override
    public Boolean townsAreImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        return this.fileUtil.readFile(TOWNS_JSON_FILE_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) {
        StringBuilder sb = new StringBuilder();
        TownImportDto[] townImportDtos = this.gson.fromJson(townsFileContent,TownImportDto[].class);

        for (TownImportDto tDto : townImportDtos) {
            if (this.validationUtil.isValid(tDto)){
                if (this.townRepository.findByName(tDto.getName()) == null){
                    Town  town = this.modelMapper.map(tDto,Town.class);
                    this.townRepository.saveAndFlush(town);
                    sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,"Town",town.getName()))
                            .append(System.lineSeparator());
                }else {
                    sb.append(DUPLICATE_DATA_MESSAGE).append(System.lineSeparator());
                }
            }else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String exportRacingTowns() {
        StringBuilder sb = new StringBuilder();
        List<Object[]> towns = this.townRepository.exsportAllTowns();
        for (Object[] town : towns) {
            sb.append(String.format("Name:%s%nRacers:%s%n%n",town[0],town[1]));
        }
        return sb.toString().trim();
    }

    @Override
    public Town findTownByName(String name) {
        return this.townRepository.findByName(name);
    }
}
