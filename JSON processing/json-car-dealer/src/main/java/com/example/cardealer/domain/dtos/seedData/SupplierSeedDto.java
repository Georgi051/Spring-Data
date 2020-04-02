package com.example.cardealer.domain.dtos.seedData;

import com.google.gson.annotations.Expose;


import javax.validation.constraints.NotNull;

public class SupplierSeedDto {
    @Expose
    private String name;
    @Expose
    private boolean isImporter;

    public SupplierSeedDto() {
    }

    @NotNull(message = "Name cannot be null!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImporter() {
        return isImporter;
    }

    public void setImporter(boolean importer) {
        isImporter = importer;
    }
}
