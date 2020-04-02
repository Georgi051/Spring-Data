package com.example.xmlproductshop.servises;

import com.example.xmlproductshop.models.dtos.Query3.CategoriesRootDto;
import com.example.xmlproductshop.models.dtos.seedData.CategorySeedDto;
import com.example.xmlproductshop.models.entities.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    void seedCategory(List<CategorySeedDto> categorySeedDtos);

    Set<Category> getRandomCategories();

    CategoriesRootDto getAllCategoriesByProductCount();
}
