package com.example.cardealer.services;

import com.example.cardealer.domain.dtos.seedData.PartSeedDto;
import com.example.cardealer.domain.entities.Part;


import java.util.Set;

public interface PartService {
    void seedParts(PartSeedDto[] partSeedDto);

    Set<Part> getRandomParts();
}
