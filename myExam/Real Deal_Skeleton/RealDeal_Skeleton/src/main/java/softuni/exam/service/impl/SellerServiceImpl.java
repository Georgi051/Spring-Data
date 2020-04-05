package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Seller;
import softuni.exam.models.dtos.xmlDtos.SellerSeedDto;
import softuni.exam.models.dtos.xmlDtos.SellerSeedViewDto;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static softuni.exam.constants.GlobalConstants.INVALID_MASSAGE;
import static softuni.exam.constants.GlobalConstants.SELLERS_PATH;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper modelMapper, FileUtil fileUtil, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return this.fileUtil.readFile(SELLERS_PATH);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        SellerSeedViewDto sDto = this.xmlParser.unmarshalFromFile(SELLERS_PATH,SellerSeedViewDto.class);
        for (SellerSeedDto sellerSeedDto : sDto.getSellerList()) {
            if (this.validationUtil.isValid(sellerSeedDto)){
                if (this.sellerRepository.findByFirstNameAndLastNameAndEmailAndRating(sellerSeedDto.getFirstName()
                        ,sellerSeedDto.getLastName(),sellerSeedDto.getEmail(),sellerSeedDto.getRating()) == null){
                    Seller seller = this.modelMapper.map(sellerSeedDto,Seller.class);
                    this.sellerRepository.saveAndFlush(seller);
                    sb.append(String.format("Successfully imported picture - %s - %s",
                            seller.getLastName(),seller.getEmail())).append(System.lineSeparator());
                }else {
                    sb.append(String.format(INVALID_MASSAGE,"seller")).append(System.lineSeparator());
                }
            }else {
                sb.append(String.format(INVALID_MASSAGE,"seller")).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }

    @Override
    public Seller getById(Long id) {
        return this.sellerRepository.findFirstById(id);
    }
}
