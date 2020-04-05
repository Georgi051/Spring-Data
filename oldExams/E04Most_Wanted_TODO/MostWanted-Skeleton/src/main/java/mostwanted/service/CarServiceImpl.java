package mostwanted.service;

import com.google.gson.Gson;
import mostwanted.domain.dtos.CarImportDto;
import mostwanted.domain.entities.Car;
import mostwanted.domain.entities.Racer;
import mostwanted.repository.CarRepository;
import mostwanted.util.FileUtil;
import mostwanted.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static mostwanted.common.Constants.*;


@Service
public class CarServiceImpl implements CarService{

    private final static String CARS_JSON_FILE_PATH = System.getProperty("user.dir")+"/src/main/resources/files/cars.json";

    private final CarRepository carRepository;
    private final RacerService racerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final FileUtil fileUtil;
    private final Gson gson;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, RacerService racerService, ModelMapper modelMapper, ValidationUtil validationUtil, FileUtil fileUtil, Gson gson) {
        this.carRepository = carRepository;
        this.racerService = racerService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }

    @Override
    public Boolean carsAreImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsJsonFile() throws IOException {
        return this.fileUtil.readFile(CARS_JSON_FILE_PATH);
    }

    @Override
    public String importCars(String carsFileContent)  {
        StringBuilder sb = new StringBuilder();
        CarImportDto[] carImportDtos = this.gson.fromJson(carsFileContent,CarImportDto[].class);
        for (CarImportDto carDto : carImportDtos) {
         if (this.validationUtil.isValid(carDto)){
             Racer racer = this.racerService.findByName(carDto.getRacerName());
                 if (racer == null){
                     sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
                 }else {
                     Car car = this.modelMapper.map(carDto,Car.class);
                     car.setRacer(racer);
                     this.carRepository.saveAndFlush(car);
                     sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,car.getBrand() + " " + car.getModel()
                             ,car.getYearOfProduction()))
                             .append(System.lineSeparator());
                 }
         }else {
             sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
         }
        }
        return sb.toString().trim();
    }

    @Override
    public Car findById(Integer id) {
        return this.carRepository.findById(id).orElse(null);
    }
}
