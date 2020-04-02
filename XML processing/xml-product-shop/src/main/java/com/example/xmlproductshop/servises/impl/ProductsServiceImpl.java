package com.example.xmlproductshop.servises.impl;

import com.example.xmlproductshop.models.dtos.Query1.ProductDto;
import com.example.xmlproductshop.models.dtos.Query1.ProductsRootDto;
import com.example.xmlproductshop.models.dtos.seedData.ProductSeedDto;
import com.example.xmlproductshop.models.entities.Product;
import com.example.xmlproductshop.models.entities.User;
import com.example.xmlproductshop.repositories.ProductRepository;
import com.example.xmlproductshop.servises.CategoryService;
import com.example.xmlproductshop.servises.ProductService;
import com.example.xmlproductshop.servises.UserService;
import com.example.xmlproductshop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class ProductsServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ValidationUtil validationUtil;
    private ModelMapper modelMapper;
    private UserService userService;
    private CategoryService categoryService;

    @Autowired
    public ProductsServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil, ModelMapper modelMapper, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProduct(List<ProductSeedDto> productSeedDtos) {
        if (this.productRepository.count() != 0) {
            return;
        }

        productSeedDtos.forEach(p -> {
            if (this.validationUtil.isValid(p)) {
                if (this.productRepository.findByNameAndPrice(p.getName(), p.getPrice()) == null) {
                    Product product = this.modelMapper.map(p, Product.class);
                    Random random = new Random();

                    User buyer = this.userService.getRandomUser();
                    User seller = this.userService.getRandomUser();

                    int randomBuyerId = random.nextInt(2);

                    if (randomBuyerId == 1) {
                        product.setBuyer(buyer);
                    }

                    product.setSeller(seller);
                    product.setCategories(this.categoryService.getRandomCategories());
                    this.productRepository.saveAndFlush(product);
                } else {
                    System.out.printf("Product %s with %s price already exist in data base!%n",
                            p.getName(), p.getPrice());
                }
            } else {
                this.validationUtil.violations(p)
                        .stream().map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public ProductsRootDto getAllProductsBetween() {

        ProductsRootDto allByPriceBetween = new ProductsRootDto();
        List<ProductDto> pDto = new ArrayList<>();
        this.productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal.valueOf(500), BigDecimal.valueOf(1000)).forEach(p -> {

            ProductDto productDto = new ProductDto();
            productDto.setName(p.getName());
            productDto.setPrice(p.getPrice());
            if (p.getSeller().getFirstName() == null) {
                productDto.setSellerName(p.getSeller().getLastName());
            } else {
                productDto.setSellerName(p.getSeller().getFirstName() + " " + p.getSeller().getLastName());
            }
            pDto.add(productDto);
        });
        allByPriceBetween.setProductDtoList(pDto);
        System.out.println();
        return allByPriceBetween;
    }
}
