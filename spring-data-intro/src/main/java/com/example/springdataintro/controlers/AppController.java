package com.example.springdataintro.controlers;

import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.services.BookService;
import com.example.springdataintro.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class AppController implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    public AppController(CategoryService categoryService1, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService1;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();

        //Just play run button

        System.out.println("Choose query number 1-4:");
        int query = Integer.parseInt(scanner.nextLine());
        switch (query) {
            case 1:
                allBookReleaseAfter();
                break;
            case 2:
                allAuthorMoreThanOneBookBefore();
                break;
            case 3:
                getAllAuthorsSortedInDescByCountBooks();
                break;
            case 4:
                findAllBookForCurrentAuthor();
                break;
            default:
                System.out.println("Wrong query number!");
        }
    }

    private void findAllBookForCurrentAuthor() {
        bookService.getAllBookFromCurrentAuthor().forEach(System.out::println);
    }

    private void getAllAuthorsSortedInDescByCountBooks() {
        authorService.getAuthorsOrderByBookDesc().forEach(System.out::println);
    }

    private void allAuthorMoreThanOneBookBefore() {
        this.bookService.findAllBooksBefore1990().forEach(System.out::println);
    }

    private void allBookReleaseAfter() {
        this.bookService.findAllBooksAfter2000().forEach(b -> System.out.println(b.getTitle()));
    }
}
