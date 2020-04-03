package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.SeedData.EmployeeCardsSeedDto;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final FileUtil fileUtil;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, FileUtil fileUtil) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.fileUtil = fileUtil;
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count() > 0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEES_CARDS_PATH);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) {
        StringBuilder sb = new StringBuilder();
        EmployeeCardsSeedDto[] employeeCardsSeedDtos = this.gson.fromJson(employeeCardsFileContent,EmployeeCardsSeedDto[].class);
        for (EmployeeCardsSeedDto eDto : employeeCardsSeedDtos) {
            if (this.validationUtil.isValid(eDto)){
                EmployeeCard employeeCard = this.modelMapper.map(eDto,EmployeeCard.class);

                EmployeeCard byNumber = this.employeeCardRepository.findByNumber(employeeCard.getNumber());
                if (byNumber == null){
                    this.employeeCardRepository.saveAndFlush(employeeCard);
                    sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE
                            ,employeeCard.getClass().getSimpleName(), employeeCard.getNumber()))
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
    public EmployeeCard findByNumber(String number) {
        return employeeCardRepository.findByNumber(number);
    }
}
