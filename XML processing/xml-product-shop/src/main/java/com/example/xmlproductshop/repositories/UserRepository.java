package com.example.xmlproductshop.repositories;

import com.example.xmlproductshop.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstNameAndLastNameAndAge(String firstName, String lastName, Integer age);

    @Query("SELECT u from User as u where " +
            "u.productSells.size > 0" +
            " order by u.lastName,u.firstName")
    List<User> findAllByProductBuysGreaterThanOrderBy();


    List<User> getAllByProductSellsGreaterThanOrderByProductSellsDescLastNameAsc(int num);
}
