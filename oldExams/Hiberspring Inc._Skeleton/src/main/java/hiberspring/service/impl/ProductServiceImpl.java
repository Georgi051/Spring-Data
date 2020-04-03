package hiberspring.service.impl;

import hiberspring.domain.dtos.SeedData.ProductsSeedDto;
import hiberspring.domain.dtos.SeedData.ProductsSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.ProductService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.apache.coyote.Constants;
import org.aspectj.apache.bcel.classfile.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static hiberspring.common.Constants.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final FileUtil fileUtil;
    private final BranchServiceImpl branchService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, FileUtil fileUtil, BranchServiceImpl branchService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.fileUtil = fileUtil;
        this.branchService = branchService;
    }


    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;
    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return this.fileUtil.readFile(PRODUCTS_PATH);
    }

    @Override
    public String importProducts() throws JAXBException, IOException {
        StringBuilder sb = new StringBuilder();
        ProductsSeedRootDto productsSeedRootDto =
                this.xmlParser.unmarshalFromFile(PRODUCTS_PATH,ProductsSeedRootDto.class);
        for (ProductsSeedDto pDto : productsSeedRootDto.getProductList()) {
            if (this.validationUtil.isValid(pDto)){
                Product product = this.modelMapper.map(pDto,Product.class);
                Branch branch = this.branchService.findByName(pDto.getBranch());
                product.setBranch(branch);
                this.productRepository.saveAndFlush(product);
                sb.append(String.format(SUCCESSFUL_IMPORT_MESSAGE,
                        product.getClass().getSimpleName(),
                        product.getName()))
                        .append(System.lineSeparator());
            }else {
                sb.append(INCORRECT_DATA_MESSAGE).append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}
