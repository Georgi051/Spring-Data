package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.SeedData.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.apache.tomcat.jni.Global;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final TownServiceImpl townService;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, FileUtil fileUtil, TownServiceImpl townService) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.townService = townService;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return this.fileUtil.readFile(BRANCHES_PATH);
    }

    @Override
    public String importBranches(String branchesFileContent) {
        StringBuilder sb = new StringBuilder();
        BranchSeedDto[] branchSeedDtos = this.gson.fromJson(branchesFileContent,BranchSeedDto[].class);
        for (BranchSeedDto branchSeedDto : branchSeedDtos) {
         if (this.validationUtil.isValid(branchSeedDto)){
             Branch branch = this.modelMapper.map(branchSeedDto,Branch.class);
             Town town = this.townService.findByName(branchSeedDto.getTown());
             if (town != null){
                 branch.setTown(town);
                 this.branchRepository.saveAndFlush(branch);
                 sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE
                         ,branch.getClass().getSimpleName(),branch.getName()))
                         .append(System.lineSeparator());
             }else {
                 sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
             }
         }else {
             sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
         }
        }
        return sb.toString().trim();
    }

    @Override
    public Branch findByName(String name) {
       return this.branchRepository.findByName(name);
    }
}
