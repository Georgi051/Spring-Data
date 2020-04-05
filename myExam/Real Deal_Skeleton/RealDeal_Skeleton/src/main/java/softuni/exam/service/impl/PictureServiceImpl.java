package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Picture;
import softuni.exam.models.dtos.PicturesSeedDto;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static softuni.exam.constants.GlobalConstants.INVALID_MASSAGE;
import static softuni.exam.constants.GlobalConstants.PICTURES_PATH;

@Service
@Transactional
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CarService carService;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, Gson gson, CarService carService) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.carService = carService;
    }


    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return this.fileUtil.readFile(PICTURES_PATH);
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();
        PicturesSeedDto[] pDto = this.gson.fromJson(this.fileUtil.readFile(PICTURES_PATH),PicturesSeedDto[].class);
        for (PicturesSeedDto pictureSeedDto : pDto) {
            if (this.validationUtil.isValid(pictureSeedDto)){
                if (this.pictureRepository.findByName(pictureSeedDto.getName()) == null){

                    Car car = this.carService.getById(pictureSeedDto.getCar());

                    if (car != null){
                        Picture picture = new Picture();
                        picture.setName(pictureSeedDto.getName());
                        picture.setCar(car);
                        LocalDateTime date = getDate(pictureSeedDto.getDateAndTime());
                        picture.setDateAndTime(date);

                        this.pictureRepository.saveAndFlush(picture);
                        sb.append(String.format("Successfully imported picture - %s",
                                picture.getName())).append(System.lineSeparator());
                    }else {
                        sb.append(String.format(INVALID_MASSAGE,"picture")).append(System.lineSeparator());
                    }
                }else {
                    sb.append(String.format(INVALID_MASSAGE,"picture")).append(System.lineSeparator());
                }
            }else {
                sb.append(String.format(INVALID_MASSAGE,"picture")).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    private LocalDateTime getDate(String dateAndTime) {
        return LocalDateTime.parse(dateAndTime, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
    }
}
