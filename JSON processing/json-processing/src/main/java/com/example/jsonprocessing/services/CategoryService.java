package com.example.jsonprocessing.services;


import com.example.jsonprocessing.models.dtos.thirdQuery.CategoryByCountingDto;
import com.example.jsonprocessing.models.dtos.seedData.CategorySeedDto;
import com.example.jsonprocessing.models.entities.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategory(CategorySeedDto[] categorySeedDto);

    Set<Category> getRandomCategories();

    List<CategoryByCountingDto> getAllCategoryByCount();
}
