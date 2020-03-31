package com.example.springdataintro.repository;

import com.example.springdataintro.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;


public interface AuthorRepository extends JpaRepository<Author, Long> {
    Set<Author> getAllBy();
    Author findByFirstNameAndLastName(String first,String last);
}
