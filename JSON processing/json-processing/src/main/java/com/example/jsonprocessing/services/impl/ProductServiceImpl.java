package com.example.jsonprocessing.services.impl;

import com.example.jsonprocessing.models.dtos.firstQuery.ProductInRangeDto;
import com.example.jsonprocessing.models.dtos.seedData.ProductSeedDto;
import com.example.jsonprocessing.models.dtos.fourQuery.ProductWithBuyerDto;
import com.example.jsonprocessing.models.dtos.secondQuery.SellerWithLeastOneProductDto;
import com.example.jsonprocessing.models.entities.Product;
import com.example.jsonprocessing.repositories.ProductRepository;
import com.example.jsonprocessing.services.ProductService;
import com.example.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ValidationUtil validationUtil, UserServiceImpl userService, CategoryServiceImpl categoryService) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedProducts(ProductSeedDto[] productSeedDto) {
        if (this.productRepository.count() != 0) {
            return;
        }

        Arrays.stream(productSeedDto).forEach(pDto -> {
            if (this.validationUtil.isValid(pDto)) {
                if (checkForExisting(pDto)) {
                    System.out.printf("%s already exist%n", pDto.getName());
                    return;
                }
                Product product = this.modelMapper.map(pDto, Product.class);

                product.setSeller(this.userService.getRandomUser());
                Random random = new Random();
                int randomBuyerId = random.nextInt(2);
                if (randomBuyerId == 1) {
                    product.setBuyer(this.userService.getRandomUser());
                }
                product.setCategories(this.categoryService.getRandomCategories());
                this.productRepository.saveAndFlush(product);
            } else {
                this.validationUtil.getViolation(pDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public List<ProductInRangeDto> getProductInRange() {
        return this.productRepository.getAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal.valueOf(500), BigDecimal.valueOf(1000))
                .stream()
                .map(p -> {
                    ProductInRangeDto productInRangeDto = this.modelMapper.map(p, ProductInRangeDto.class);
                    productInRangeDto.setSeller(String.format("%s %s",
                            p.getSeller().getFirstName(),
                            p.getSeller().getLastName()));
                    return productInRangeDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SellerWithLeastOneProductDto> getAllProductWithBuyer() {
        List<Product> products = this.productRepository.findByBuyerIsNotNull();

        List<SellerWithLeastOneProductDto> sellerWithLeastOneProductDtoList = new ArrayList<>();
        for (Product product : products) {
            String firstName = product.getSeller().getFirstName();
            String lastName = product.getSeller().getLastName();

            Set<ProductWithBuyerDto> pDto = new LinkedHashSet<>();
            List<Product> sellerToProduct = this.productRepository.findBySellerId(product.getSeller().getId());

            for (Product p : sellerToProduct) {
                ProductWithBuyerDto oneDto = new ProductWithBuyerDto();

                oneDto.setName(p.getName());
                oneDto.setPrice(p.getPrice());
                oneDto.setBuyerFirstName(p.getBuyer().getFirstName());
                oneDto.setBuyerLastName(p.getBuyer().getLastName());
                pDto.add(oneDto);
            }

            SellerWithLeastOneProductDto sDto = new SellerWithLeastOneProductDto();
            sDto.setFirstName(firstName);
            sDto.setLastName(lastName);
            sDto.setSoldProducts(pDto);

            boolean isFound = true;
            for (SellerWithLeastOneProductDto seller : sellerWithLeastOneProductDtoList) {
                if (seller.getLastName().equals(product.getSeller().getLastName())) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                sellerWithLeastOneProductDtoList.add(sDto);
            }
        }
        return sellerWithLeastOneProductDtoList;
    }


    private boolean checkForExisting(ProductSeedDto pDto) {
        Product product = this.productRepository.findFirstByName(pDto.getName());
        return product != null;
    }
}