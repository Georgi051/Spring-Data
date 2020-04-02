package com.example.xmlproductshop.repositories;

import com.example.xmlproductshop.models.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    @Query("SELECT c from Category as c" +
            " join Product as p on c.id = p.id " +
            "group by c.name " +
            "order by c.products.size desc")
    List<Category> getAllByOrderByProducts();
}
