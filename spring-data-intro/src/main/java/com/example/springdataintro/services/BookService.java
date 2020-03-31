package com.example.springdataintro.services;

import com.example.springdataintro.entities.Book;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfter2000();

    Set<String> findAllBooksBefore1990();

    List<String> getAllBookFromCurrentAuthor();
}
