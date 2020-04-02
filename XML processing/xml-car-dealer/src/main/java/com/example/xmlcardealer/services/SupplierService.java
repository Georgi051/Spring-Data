package com.example.xmlcardealer.services;

import com.example.xmlcardealer.domain.dtos.Query3.LocalSuppliersRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.SupplierSeedDto;
import com.example.xmlcardealer.domain.entities.Supplier;

import java.util.List;

public interface SupplierService {
    void seedSupplier(List<SupplierSeedDto> categorySeedDto);

    Supplier getRandomSupplier();

    LocalSuppliersRootDto getAllLocalSuppliers(boolean value);
}
