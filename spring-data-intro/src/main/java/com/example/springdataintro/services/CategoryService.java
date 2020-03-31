package com.example.springdataintro.services;


import com.example.springdataintro.entities.Category;

import java.io.IOException;

public interface CategoryService {
    void  seedCategories() throws IOException;

    int getAllCategoryCount();

    Category findCategoryById(long id);
}
