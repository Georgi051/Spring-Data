package com.example.xmlproductshop.repositories;

import com.example.xmlproductshop.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByNameAndPrice(String name, BigDecimal price);

    List<Product> findAllByPriceBetweenAndBuyerIsNullOrderByPrice(BigDecimal f, BigDecimal s);

    @Query("select p from Product as p " +
            "where p.buyer.id is not null group by p.buyer order by p.seller.lastName, p.seller.firstName")
    List<Product> findByBuyerIsNotNull();

}
