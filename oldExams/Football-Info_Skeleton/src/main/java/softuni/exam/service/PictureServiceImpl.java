package softuni.exam.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dtos.pictureSeed.PictureSeedViewDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static softuni.exam.constants.GlobalConstants.*;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validatorUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validatorUtil, XmlParser xmlParser) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validatorUtil = validatorUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public String importPictures() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();

        PictureSeedViewDto pDto = this.xmlParser.unmarshalFromFile(PICTURE_SEED,PictureSeedViewDto.class);

            pDto.getPictures().forEach(pictureSeedDto -> {
                if (this.validatorUtil.isValid(pictureSeedDto)){
                    if (this.pictureRepository.findByUrl(pictureSeedDto.getUrl()) == null){
                        Picture picture = this.modelMapper.map(pictureSeedDto,Picture.class);
                        this.pictureRepository.saveAndFlush(picture);
                        sb.append(String.format(VALID_DATA,picture.getClass().getSimpleName().toLowerCase()
                                ,picture.getUrl())).append(System.lineSeparator());
                    }else {
                        sb.append("Picture is already in data base")
                                .append(System.lineSeparator());
                    }
                }else {
                    sb.append("Invalid Picture")
                            .append(System.lineSeparator());
                }
            });
        return sb.toString();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(Path.of(PICTURE_SEED));
    }

    @Override
    public Picture findByUrl(String name) {
        return this.pictureRepository.findByUrl(name);
    }
}
