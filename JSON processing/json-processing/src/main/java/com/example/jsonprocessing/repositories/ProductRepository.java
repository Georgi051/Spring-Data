package com.example.jsonprocessing.repositories;

import com.example.jsonprocessing.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findFirstByName(String name);

    List<Product> getAllByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal f, BigDecimal s);

    @Query("select p from Product as p where p.buyer.id is not null group by p.buyer order by p.seller.lastName, p.seller.firstName")
    List<Product> findByBuyerIsNotNull();

    @Query("SELECT p from Product as p where p.seller.id = :id  ORDER BY p.buyer.lastName,p.buyer.firstName")
    List<Product> findBySellerId(@Param("id") long id);

}
