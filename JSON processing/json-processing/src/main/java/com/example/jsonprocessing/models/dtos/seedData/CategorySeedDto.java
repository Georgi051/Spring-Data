package com.example.jsonprocessing.models.dtos.seedData;

import com.google.gson.annotations.Expose;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CategorySeedDto {
    @Expose
    private String name;

    public CategorySeedDto() {
    }

    @NotNull(message = "Name cannot be null.")
    @Length(min = 3, max = 15, message = "Invalid category name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
