package com.example.jsonprocessing.repositories;

import com.example.jsonprocessing.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByFirstNameAndLastName(String first, String last);

    @Query("SELECT u from User as u " +
            "JOIN Product as p ON p.id = u.id where " +
            "p.seller.productSells.size is NOT null" +
            " order by u.productSells.size desc ,u.lastName")
    List<User> findAllByProductSellsIsNotNull();

}
