package com.example.springdataintro.services;

import com.example.springdataintro.entities.Book;

import java.io.IOException;
import java.util.List;


public interface BookService {
    void seedBooks() throws IOException;
    List<Book> getAllBooksWithCurrentAgeRestriction(String ageRestriction);

    List<Book> getAllBooksLessThan5000Copies();

    List<Book> getAllBooksWithPriceLessThan5AndHigherThan40();

    List<Book> getAllBooksNotInReleasedDate(int date);

    List<Book> getAllBooksBeforeCurrentYear(String year);

    List<Book> getAllBooksContainsInTitle(String letters);

    List<Book> getAllBooksWhereAuthorLastNameStartsWith(String input);

    int getAllBooksWithCurrentTitleLength(int num);

    List<Object[]> getAllAuthorsWithCopiesInDescOrder();

    Book getBookByTitle(String title);
}
