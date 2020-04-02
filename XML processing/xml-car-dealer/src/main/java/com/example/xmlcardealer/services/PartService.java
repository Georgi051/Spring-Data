package com.example.xmlcardealer.services;

import com.example.xmlcardealer.domain.dtos.seedData.PartSeedDto;
import com.example.xmlcardealer.domain.entities.Part;

import java.util.List;
import java.util.Set;

public interface PartService {
    void seedParts(List<PartSeedDto> partSeedDto);

    Set<Part> getRandomParts();
}
