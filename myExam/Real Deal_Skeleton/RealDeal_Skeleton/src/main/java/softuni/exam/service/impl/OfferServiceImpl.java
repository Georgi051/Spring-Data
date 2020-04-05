package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Offer;
import softuni.exam.models.Picture;
import softuni.exam.models.Seller;
import softuni.exam.models.dtos.xmlDtos.orderDtos.OfferDto;
import softuni.exam.models.dtos.xmlDtos.orderDtos.OffеrsViewDto;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static softuni.exam.constants.GlobalConstants.INVALID_MASSAGE;
import static softuni.exam.constants.GlobalConstants.OFFERS_PATH;

@Service
@Transactional
public class OfferServiceImpl implements OfferService {

    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private OfferRepository offerRepository;
    private SellerService sellerService;
    private CarService carService;

    public OfferServiceImpl(ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, XmlParser xmlParser, OfferRepository offerRepository, SellerService sellerService, CarService carService) {
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.offerRepository = offerRepository;
        this.sellerService = sellerService;
        this.carService = carService;
    }


    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return this.fileUtil.readFile(OFFERS_PATH);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        OffеrsViewDto oDto = this.xmlParser.unmarshalFromFile(OFFERS_PATH,OffеrsViewDto.class);
        for (OfferDto offerDto : oDto.getOfferList()) {
         if (this.validationUtil.isValid(offerDto)){
             LocalDateTime date = getDateTime(offerDto.getAddedOn());
            if (this.offerRepository.findByDescriptionAndAddedOn(offerDto.getDescription(),date) == null){

                Seller seller = this.sellerService.getById(offerDto.getSeller().getId());
                Car car = this.carService.getById(offerDto.getCar().getId());

                if (seller != null && car != null){
                    Offer offer = this.modelMapper.map(offerDto,Offer.class);
                    offer.setAddedOn(date);
                    offer.setCar(car);
                    offer.setSeller(seller);

                    List<Picture> pictures = new ArrayList<>(car.getPicture());
                    if (pictures.size() != 0){
                        offer.setPictures(pictures);
                    }

                    this.offerRepository.saveAndFlush(offer);
                    String stringData = offer.getAddedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                    sb.append(String.format("Successfully imported offer - %s - %s",
                            stringData,offer.isHasGoldStatus())).append(System.lineSeparator());
                }else {
                    sb.append(String.format(INVALID_MASSAGE,"offer")).append(System.lineSeparator());
                }
            }else {
                 sb.append(String.format(INVALID_MASSAGE,"offer")).append(System.lineSeparator());
             }
         }else {
             sb.append(String.format(INVALID_MASSAGE,"offer")).append(System.lineSeparator());
         }
        }


        return sb.toString().trim();
    }

    private LocalDateTime getDateTime(String addedOn) {
        return LocalDateTime.parse(addedOn, DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss"));
    }
}
