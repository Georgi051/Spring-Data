package com.example.springdataintro.services.impl;

import com.example.springdataintro.constants.GlobalConstants;
import com.example.springdataintro.entities.Category;
import com.example.springdataintro.repository.CategoryRepository;
import com.example.springdataintro.services.CategoryService;
import com.example.springdataintro.utils.FileUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private FileUtil fileUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository,FileUtil fileUtil) {
        this.categoryRepository = categoryRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedCategories() throws IOException {
        if (this.categoryRepository.count() != 0){
            return;
        }
        String[] fileContent = this.fileUtil.readFileContent(GlobalConstants.CATEGORIES_FILE_PATH);

        Arrays.stream(fileContent).forEach(e -> {
            Category category = new Category(e);
            this.categoryRepository.saveAndFlush(category);
        });

    }

    @Override
    public int getAllCategoryCount() {
        return (int) this.categoryRepository.count();
    }

    @Override
    public Category findCategoryById(long id) {
        return this.categoryRepository.findById(id).orElse(null);
    }
}
