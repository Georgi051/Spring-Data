package hiberspring.service.impl;

import hiberspring.domain.dtos.SeedData.EmployeeSeedDto;
import hiberspring.domain.dtos.SeedData.EmployeeSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;
    private final EmployeeCardServiceImpl employeeCardService;
    private final BranchServiceImpl branchService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, FileUtil fileUtil, EmployeeCardServiceImpl employeeCardService, BranchServiceImpl branchService) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
        this.employeeCardService = employeeCardService;
        this.branchService = branchService;
    }


    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return this.fileUtil.readFile(EMPLOYEE_PATH);
    }

    @Override
    public String importEmployees() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        EmployeeSeedRootDto employeeSeedRootDto =
                this.xmlParser.unmarshalFromFile(EMPLOYEE_PATH,EmployeeSeedRootDto.class);
        for (EmployeeSeedDto eDto : employeeSeedRootDto.getEmployeeSeedDtos()) {
            if (this.validationUtil.isValid(eDto)){
                Employee employee = this.modelMapper.map(eDto,Employee.class);

                EmployeeCard employeeCard = this.employeeCardService.findByNumber(eDto.getCard());
                Branch branch = this.branchService.findByName(eDto.getBranch());
                Employee employeeByCardNumber = this.employeeRepository.findEmployeeByCard_Number(eDto.getCard());

                if (employeeCard == null){
                    sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                }else {
                    if (branch != null){
                        if (employeeByCardNumber == null){
                            employee.setCard(employeeCard);
                            employee.setBranch(branch);
                            this.employeeRepository.saveAndFlush(employee);
                            sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE
                                    ,employee.getClass().getSimpleName()
                                    ,employee.getFirstName() + " " + employee.getLastName()))
                                    .append(System.lineSeparator());
                        }
                    }else {
                        sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                    }
                }
            }else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();

        this.employeeRepository.findAllByBranchWithProducts().stream()
       .sorted((f,s) -> {
           String firstFullName = f.getFirstName() + " " + f.getLastName();
           String secondFullName = s.getFirstName() + " " + s.getLastName();
           int i = firstFullName.compareTo(secondFullName);
           if (i == 0){
              i = s.getPosition().length() - f.getPosition().length();
           }
        return i;
       }).forEach(employee -> {
           sb.append(String.format("Name: %s",employee.getFirstName() + " " + employee.getLastName()))
                   .append(System.lineSeparator())
                   .append(String.format("Position: %s",employee.getPosition()))
                   .append(System.lineSeparator())
                   .append(String.format("Card Number: %s",employee.getCard().getNumber()))
                   .append(System.lineSeparator())
                   .append("---------------------------------------")
                   .append(System.lineSeparator());
       });
        return sb.toString().trim();
    }
}
