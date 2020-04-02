package com.example.xmlproductshop.servises.impl;

import com.example.xmlproductshop.models.dtos.Query3.CategoriesRootDto;
import com.example.xmlproductshop.models.dtos.Query3.CategoryWithProductsDto;
import com.example.xmlproductshop.models.dtos.seedData.CategorySeedDto;
import com.example.xmlproductshop.models.entities.Category;
import com.example.xmlproductshop.models.entities.Product;
import com.example.xmlproductshop.repositories.CategoryRepository;
import com.example.xmlproductshop.servises.CategoryService;
import com.example.xmlproductshop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Random random;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Random random) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedCategory(List<CategorySeedDto> categorySeedDtos) {
        if (this.categoryRepository.count() != 0) {
            return;
        }
        categorySeedDtos.forEach(c -> {
            if (this.validationUtil.isValid(c)) {
                if (this.categoryRepository.findByName(c.getName()) == null) {
                    Category category = this.modelMapper.map(c, Category.class);
                    this.categoryRepository.saveAndFlush(category);
                } else {
                    System.out.printf("%s category is already in data base!%n", c.getName());
                }
            } else {
                this.validationUtil.violations(c).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();
        long randomCategoryCount = this.random.nextInt(3) + 1;
        for (int i = 1; i <= randomCategoryCount; i++) {
            long id = this.random.nextInt((int) categoryRepository.count()) + 1;
            categories.add(this.categoryRepository.getOne(id));
        }
        return categories;
    }

    @Override
    public CategoriesRootDto getAllCategoriesByProductCount() {
        CategoriesRootDto categories = new CategoriesRootDto();

        List<CategoryWithProductsDto> categoryWithProductsDtoList = new ArrayList<>();
        this.categoryRepository.getAllByOrderByProducts().forEach
                (c -> {
                    CategoryWithProductsDto cDto = new CategoryWithProductsDto();
                    BigDecimal priceSum = c.getProducts().stream().map(Product::getPrice)
                            .reduce(BigDecimal::add).orElse(null);
                    cDto.setCategoryName(c.getName());
                    cDto.setProductsCount(c.getProducts().size());

                    BigDecimal bigDecimal = BigDecimal.valueOf(c.getProducts().size());
                    BigDecimal avgSum = priceSum.divide(bigDecimal, 6, RoundingMode.DOWN);
                    cDto.setTotalRevue(priceSum);
                    cDto.setAveragePrice(avgSum);
                    categoryWithProductsDtoList.add(cDto);
                });

        categories.setCategoryList(categoryWithProductsDtoList);
        return categories;
    }
}
