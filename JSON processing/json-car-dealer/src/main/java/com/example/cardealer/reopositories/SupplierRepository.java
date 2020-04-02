package com.example.cardealer.reopositories;

import com.example.cardealer.domain.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {
        Supplier findByName(String name);

        List<Supplier> getAllByImporter(boolean value);
}
