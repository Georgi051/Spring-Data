package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.DistrictImportDto;
import mostwanted.domain.entities.District;
import mostwanted.domain.entities.Town;
import mostwanted.repository.DistrictRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final static String DISTRICT_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/districts.json";

    private final DistrictRepository districtRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, Gson gson) {
        this.districtRepository = districtRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }


    @Override
    public Boolean districtsAreImported() {
        return this.districtRepository.count() > 0;
    }

    @Override
    public String readDistrictsJsonFile() throws IOException {
        return this.fileUtil.readFile(DISTRICT_JSON_FILE_PATH);
    }

    @Override
    public String importDistricts(String districtsFileContent) {
        StringBuilder sb = new StringBuilder();
        DistrictImportDto[] districtImportDtos = this.gson.fromJson(districtsFileContent, DistrictImportDto[].class);

        for (DistrictImportDto districtImportDto : districtImportDtos) {
            if (this.validationUtil.isValid(districtImportDto)){
                if (this.districtRepository.findByName(districtImportDto.getName()) == null){
                    Town findTown = this.townService.findTownByName(districtImportDto.getTownName());
                    if (findTown == null){
                        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                    }else {
                        District district = this.modelMapper.map(districtImportDto,District.class);
                        district.setTown(findTown);
                        this.districtRepository.saveAndFlush(district);
                        sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,"District",district.getName()))
                                .append(System.lineSeparator());
                    }
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
    public District findByName(String name) {
        return this.districtRepository.findByName(name);
    }
}
