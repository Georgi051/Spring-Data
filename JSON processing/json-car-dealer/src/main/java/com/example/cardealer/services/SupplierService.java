package com.example.cardealer.services;

import com.example.cardealer.domain.dtos.Query3.LocalSuppliersDto;
import com.example.cardealer.domain.dtos.seedData.SupplierSeedDto;
import com.example.cardealer.domain.entities.Supplier;

import java.util.List;

public interface SupplierService {
    void seedSupplier(SupplierSeedDto[] categorySeedDto);

    Supplier getRandomSupplier();

    List<LocalSuppliersDto> getAllLocalSuppliers(boolean value);
}
