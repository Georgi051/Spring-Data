package com.example.jsonprocessing.services.impl;

import com.example.jsonprocessing.models.dtos.thirdQuery.CategoryByCountingDto;
import com.example.jsonprocessing.models.dtos.seedData.CategorySeedDto;
import com.example.jsonprocessing.models.entities.Category;
import com.example.jsonprocessing.models.entities.Product;
import com.example.jsonprocessing.repositories.CategoryRepository;
import com.example.jsonprocessing.services.CategoryService;
import com.example.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public void seedCategory(CategorySeedDto[] categorySeedDto) {
        if (this.categoryRepository.count() != 0) {
            return;
        }
        Arrays.stream(categorySeedDto).forEach(cDto -> {

            if (this.validationUtil.isValid(cDto)) {
                if (checkForExisting(cDto)) {
                    System.out.printf("%s already exist%n", cDto.getName());
                    return;
                }
                Category category = this.modelMapper.map(cDto, Category.class);
                this.categoryRepository.saveAndFlush(category);
            } else {
                this.validationUtil.getViolation(cDto)
                        .stream().map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public Set<Category> getRandomCategories() {
        Random random = new Random();
        Set<Category> result = new HashSet<>();
        int count = random.nextInt(3) + 1;
        for (int i = 0; i < count; i++) {
            long rId = random.nextInt((int) categoryRepository.count()) + 1;
            result.add(categoryRepository.getOne(rId));
        }
        return result;
    }

    @Override
    public List<CategoryByCountingDto> getAllCategoryByCount() {
        List<Category> allByProductsNumberOfProducts = this.categoryRepository.getAllByProductsNumberOfProducts();

        List<CategoryByCountingDto> categoryByCountingDtos = new ArrayList<>();

        for (Category category : allByProductsNumberOfProducts) {
            CategoryByCountingDto categoryDto = new CategoryByCountingDto();
            categoryDto.setCategory(category.getName());
            categoryDto.setProductsCount(category.getProducts().size());

            BigDecimal sum = category.getProducts().stream()
                    .map(Product::getPrice).reduce(BigDecimal::add)
                    .orElse(null);

            BigDecimal bigDecimal = BigDecimal.valueOf(category.getProducts().size());
            BigDecimal avgSum = sum.divide(bigDecimal, 6, RoundingMode.DOWN);

            categoryDto.setAveragePrice(avgSum);
            categoryDto.setTotalRevenue(sum);
            categoryByCountingDtos.add(categoryDto);
        }
        return categoryByCountingDtos;
    }

    private boolean checkForExisting(CategorySeedDto categorySeedDto) {
        Category category = this.categoryRepository.findFirstByName(categorySeedDto.getName());
        return category != null;
    }
}
