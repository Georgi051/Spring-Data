package com.example.jsonprocessing.repositories;

import com.example.jsonprocessing.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findFirstByName(String name);

    @Query("SELECT c from Category as c " +
            "join Product as p " +
            "on  p.id = c.id " +
            "group by c.name " +
            "order by c.products.size desc ")
    List<Category> getAllByProductsNumberOfProducts();
}
