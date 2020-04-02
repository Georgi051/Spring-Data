package com.example.cardealer.services.impl;

import com.example.cardealer.domain.dtos.seedData.PartSeedDto;
import com.example.cardealer.domain.entities.Part;
import com.example.cardealer.reopositories.PartRepository;
import com.example.cardealer.services.PartService;
import com.example.cardealer.services.SupplierService;
import com.example.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.*;

@Service
public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final SupplierService supplierService;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper, ValidationUtil validationUtil, SupplierService supplierService) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.supplierService = supplierService;
    }

    @Override
    public void seedParts(PartSeedDto[] partSeedDto) {
        if (this.partRepository.count() != 0) {
            return;
        }
        Arrays.stream(partSeedDto).forEach(p -> {
            if (this.validationUtil.isValid(p)) {
                if (this.partRepository.findByNameAndPrice(p.getName(),p.getPrice()) == null){
                    Part part = this.modelMapper.map(p, Part.class);
                    part.setSupplier(this.supplierService.getRandomSupplier());
                    this.partRepository.saveAndFlush(part);
                }else {
                    System.out.printf("%s with price:%.2f is already in data base!%n",p.getName()
                            ,p.getPrice());
                }

            } else {
                this.validationUtil.getViolation(p)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public Set<Part> getRandomParts() {
        Set<Part> parts = new HashSet<>();
        Random r = new Random();
        int result = r.nextInt(10) + 10;
        for (int i = 0; i < result; i++) {
            long id = r.nextInt((int) this.partRepository.count()) + 1;
            parts.add(this.partRepository.getOne(id));
        }
        return parts;
    }
}
