package com.example.springdataintro.services;

import com.example.springdataintro.entities.Author;
import java.io.IOException;
import java.util.List;


public interface AuthorService {
    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById(long id);

    List<String> getAuthorsOrderByBookDesc();

    Author findAuthorByName(String first,String last);
}
