package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.dtos.CarsSeedDto;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.CARS_PATH;
import static softuni.exam.constants.GlobalConstants.INVALID_MASSAGE;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, Gson gson) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return this.fileUtil.readFile(CARS_PATH);
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();
        CarsSeedDto[] seedDto = this.gson.fromJson(fileUtil.readFile(CARS_PATH),CarsSeedDto[].class);

        for (CarsSeedDto carsSeedDto : seedDto) {
            if (this.validationUtil.isValid(carsSeedDto)){
                if (this.carRepository.findByMakeAndModelAndKilometers(carsSeedDto.getMake()
                        ,carsSeedDto.getModel(),carsSeedDto.getKilometers()) == null){
                    Car car = new Car();
                    car.setMake(carsSeedDto.getMake());
                    car.setModel(carsSeedDto.getModel());
                    car.setKilometers(carsSeedDto.getKilometers());

                    LocalDate date = getDate(carsSeedDto.getRegisteredOn());
                    car.setRegisteredOn(date);
                    this.carRepository.saveAndFlush(car);

                   sb.append(String.format("Successfully imported car - %s - %s",
                           car.getMake(),car.getModel())).append(System.lineSeparator());
                }else {
                    sb.append(String.format(INVALID_MASSAGE,"car"))
                            .append(System.lineSeparator());
                }
            }else {
                sb.append(String.format(INVALID_MASSAGE,"car"))
                        .append(System.lineSeparator());
            }

        }
        return sb.toString().trim();
    }

    private LocalDate getDate(String registeredOn) {
        return LocalDate.parse(registeredOn, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();
        List<Car> allBy = carRepository.findAllBy();

        for (Car car : allBy) {
            String date = car.getRegisteredOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            sb.append(String.format("Car make - %s, model - %s%n",car.getMake(),car.getModel()))
                    .append(String.format("        Kilometers - %d%n",car.getKilometers()))
                    .append(String.format("        Registered on - %s%n",date))
                    .append(String.format("        Number of pictures - %d%n%n",car.getPicture().size()));
        }
        return sb.toString().trim();
    }

    @Override
    public Car getById(Long id) {
        return this.carRepository.findFirstById(id);
    }
}
