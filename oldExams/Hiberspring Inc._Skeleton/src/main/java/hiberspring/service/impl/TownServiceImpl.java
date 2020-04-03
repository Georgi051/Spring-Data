package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.SeedData.TownSeedDto;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class TownServiceImpl implements TownService {
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
        return this.fileUtil.readFile(TOWN_PATH);
    }

    @Override
    public String importTowns(String townsFileContent) {
        StringBuilder sb = new StringBuilder();
        TownSeedDto[] tDto = this.gson.fromJson(townsFileContent,TownSeedDto[].class);
        for (TownSeedDto townSeedDto : tDto) {
            if (this.validationUtil.isValid(townSeedDto)){
                Town town = this.modelMapper.map(townSeedDto,Town.class);
                this.townRepository.saveAndFlush(town);
                sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,town.getClass().getSimpleName(),town.getName()))
                        .append(System.lineSeparator());
            }else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public Town findByName(String name) {
        return this.townRepository.findByName(name);
    }
}
