package com.example.xmlcardealer.services.impl;

import com.example.xmlcardealer.domain.dtos.Query3.LocalSuppliersDto;
import com.example.xmlcardealer.domain.dtos.Query3.LocalSuppliersRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.SupplierSeedDto;
import com.example.xmlcardealer.domain.entities.Supplier;
import com.example.xmlcardealer.repositories.SupplierRepository;
import com.example.xmlcardealer.services.SupplierService;
import com.example.xmlcardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedSupplier(List<SupplierSeedDto> supplierSeedDto) {
        if (this.supplierRepository.count() != 0) {
            return;
        }

        supplierSeedDto
                .forEach(s -> {
                    if (this.supplierRepository.findByName(s.getName()) == null) {
                        if (this.validationUtil.isValid(s)) {
                            Supplier supplier = this.modelMapper.map(s, Supplier.class);
                            this.supplierRepository.saveAndFlush(supplier);
                        } else {
                           this.validationUtil.violations(s)
                                    .stream().map(ConstraintViolation::getMessage)
                                    .forEach(System.out::println);
                        }
                    } else {
                        System.out.printf("Supplier with name : %s already exist in data base!%n", s.getName());
                    }
                });
    }

    @Override
    public Supplier getRandomSupplier() {
        Random random = new Random();
        long id = random.nextInt((int) this.supplierRepository.count()) + 1;
        return this.supplierRepository.getOne(id);
    }

    @Override
    public LocalSuppliersRootDto getAllLocalSuppliers(boolean value) {
        LocalSuppliersRootDto lDto = new LocalSuppliersRootDto();
        List<LocalSuppliersDto> localSuppliersDtos = this.supplierRepository.getAllByImporter(value)
                .stream()
                .map(s -> {
                    LocalSuppliersDto localSuppliersDto = this.modelMapper.map(s, LocalSuppliersDto.class);
                    localSuppliersDto.setPartsCount(s.getParts().size());
                    return localSuppliersDto;
                }).collect(Collectors.toList());
        lDto.setSupplierList(localSuppliersDtos);
        return lDto;
    }
}
