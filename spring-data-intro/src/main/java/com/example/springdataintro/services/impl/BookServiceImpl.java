package com.example.springdataintro.services.impl;

import com.example.springdataintro.entities.*;
import com.example.springdataintro.repository.BookRepository;
import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.services.BookService;
import com.example.springdataintro.services.CategoryService;
import com.example.springdataintro.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.springdataintro.constants.GlobalConstants.BOOKS_FILE_PATH;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil.readFileContent(BOOKS_FILE_PATH);

        for (String line : fileContent) {
            String[] lineParams = line.split("\\s+");
            Book book = new Book();

            book.setAuthor(setRandomAuthor());

            EditionType editionType = EditionType.values()[Integer.parseInt(lineParams[0])];
            book.setEditionType(editionType);

            DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate date = LocalDate.parse(lineParams[1], format);
            book.setReleaseDate(date);

            int copies = Integer.parseInt(lineParams[2]);
            book.setCopies(copies);

            BigDecimal price = new BigDecimal(lineParams[3]);
            book.setPrice(price);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(lineParams[4])];
            book.setAgeRestriction(ageRestriction);

            StringBuilder sb = new StringBuilder();
            for (int i = 5; i < lineParams.length; i++) {
                sb.append(lineParams[i]).append(" ");
            }
            String title = sb.toString().trim();
            book.setTitle(title);

            Set<Category> categories = getRandomCategories();
            book.setCategories(categories);

            this.bookRepository.saveAndFlush(book);
        }
    }


    @Override
    public List<Book> findAllBooksAfter2000() {
        return bookRepository.findAllByReleaseDateAfter(LocalDate.parse("2000-12-31"));
    }

    @Override
    public Set<String> findAllBooksBefore1990() {
        List<Book> books = this.bookRepository.findAllByReleaseDateBefore(LocalDate.parse("1990-01-01"));

    return books.stream()
                .map(b -> String.format("%s %s", b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> getAllBookFromCurrentAuthor() {
        Author author = authorService.findAuthorByName("George","Powell");
        List<Book> books = bookRepository.getBooksByAuthorLikeOrderByReleaseDateDescTitleAsc(author);
        return books.stream().map(b -> String.format("%s %s %d",b.getTitle(),b.getReleaseDate(),b.getCopies()))
                .collect(Collectors.toList());
    }

    private Set<Category> getRandomCategories() {
        Set<Category> categorySet = new LinkedHashSet<>();
        Random random = new Random();
        int count = random.nextInt(5);
        for (int i = 1; i <= count; i++) {
            Category category = getRandomCategory();
            categorySet.add(category);
        }
        return categorySet;
    }

    private Category getRandomCategory() {
        Random random = new Random();
        int randomId = random.nextInt(categoryService.getAllCategoryCount()) + 1;
        return this.categoryService.findCategoryById(randomId);
    }

    private Author setRandomAuthor() {
        Random random = new Random();
        int randomId = random.nextInt(authorService.getAllAuthorsCount()) + 1;
        return this.authorService.findAuthorById(randomId);
    }
}
