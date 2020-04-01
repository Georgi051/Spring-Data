package com.example.springdataautomappingdemo.repositories;

import com.example.springdataautomappingdemo.domain.entities.Game;
import com.example.springdataautomappingdemo.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<User, Game> {

}
