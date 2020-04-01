package com.example.springdataintro.controlers;

import com.example.springdataintro.entities.Book;
import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.services.BookService;
import com.example.springdataintro.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;


@Controller
public class AppController implements CommandLineRunner {
    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    @Autowired
    public AppController(CategoryService categoryService1, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService1;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();

        System.out.println("Chose task number:");
        String startProgram = bufferedReader.readLine();
        while (!startProgram.equals("end")){
            if (startProgram.length() == 1){
                if (!(Character.isDigit(startProgram.charAt(0)))){
                    ErrorMassage();
                    startProgram = bufferedReader.readLine();
                    continue;
                }
            }else if (startProgram.length() == 2){
                if (!(Character.isDigit(startProgram.charAt(0)) && Character.isDigit(startProgram.charAt(1)))){
                    ErrorMassage();
                    startProgram = bufferedReader.readLine();
                    continue;
                }
            }else {
                ErrorMassage();
                startProgram = bufferedReader.readLine();
                continue;
            }
            int task = Integer.parseInt(startProgram);
            switch (task) {
                case 1:
                    getBookTitlesByAgeRestriction();
                    break;
                case 2:
                    getAllBookTitleForGoldEditionLessThan5000copies();
                    break;
                case 3:
                    getAllBooksLessThan5OrHigher50();
                    break;
                case 4:
                    getBooksWhereIsNotInCurrentYear();
                    break;
                case 5:
                    getAllBookBeforeCurrentDate();
                    break;
                case 6:
                    getAuthorsWhereFirstNameEndsWith();
                    break;
                case 7:
                    getAllBooksWhereContainingInThereTitleCurrentLetters();
                    break;
                case 8:
                    getAllBookWithAuthorsLastNameStartsWith();
                    break;
                case 9:
                    getAllBooksWithTitleLongerThanInputNum();
                    break;
                case 10:
                    getAllAuthorsAndThereBookCopiesInDesc();
                    break;
                case 11:
                    getBookInfoWithBookTitle();
                    break;
                default:
                    System.out.println("Wrong task number!!!");
            }
            System.out.printf("%n%s","Type different task (number) or type (end):");
            startProgram = bufferedReader.readLine();
        }
    }

    private void ErrorMassage() {
        System.out.printf("%s%n%n%s", "YOU TYPE WRONG COMMAND!!!"
                , "Enter task (number) OR type (end) to stop the program:");
    }

    private void getBookInfoWithBookTitle() throws IOException {
        System.out.println("Enter book title:");
        Book b = this.bookService.getBookByTitle(this.bufferedReader.readLine());
        System.out.printf("%s %s %s %.2f%n",b.getTitle(),b.getEditionType(),b.getAgeRestriction(),b.getPrice());
    }

    private void getAllAuthorsAndThereBookCopiesInDesc() {
        this.bookService.getAllAuthorsWithCopiesInDescOrder()
                .forEach(b -> System.out.printf("%s - %s%n",b[0],b[1]));
    }

    private void getAllBooksWithTitleLongerThanInputNum() throws IOException {
        System.out.println("Enter number for length:");
        System.out.println(this.bookService.getAllBooksWithCurrentTitleLength(Integer.parseInt(bufferedReader.readLine())));
    }

    private void getAllBookWithAuthorsLastNameStartsWith() throws IOException {
        System.out.println("Enter you pattern:");
        this.bookService.getAllBooksWhereAuthorLastNameStartsWith(this.bufferedReader.readLine())
        .forEach(b-> System.out.printf("%s (%s %s)%n",b.getTitle(),b.getAuthor().getFirstName(),
                b.getAuthor().getLastName()));
    }

    private void getAllBooksWhereContainingInThereTitleCurrentLetters() throws IOException {
        System.out.println("Enter letter/s:");
        this.bookService.getAllBooksContainsInTitle(bufferedReader.readLine())
                .forEach(b -> System.out.println(b.getTitle()));
    }

    private void getAuthorsWhereFirstNameEndsWith() throws IOException {
        System.out.println("Enter letter/s:");
        this.authorService.getAllAuthorsContainsCurrentLetters(this.bufferedReader.readLine())
                .forEach(a-> System.out.printf("%s %s%n",a.getFirstName(),a.getLastName()));
    }

    private void getAllBookBeforeCurrentDate() throws IOException {
        System.out.println("Enter date:");
        this.bookService.getAllBooksBeforeCurrentYear(this.bufferedReader.readLine())
                .forEach(b -> System.out.printf("%s %s %s%n", b.getTitle(), b.getEditionType(), b.getPrice()));
    }

    private void getBooksWhereIsNotInCurrentYear() throws IOException {
        System.out.println("Enter year:");
        bookService.getAllBooksNotInReleasedDate(Integer.parseInt(this.bufferedReader.readLine()))
                .forEach(b -> System.out.println(b.getTitle()));
    }

    private void getAllBooksLessThan5OrHigher50() {
        bookService.getAllBooksWithPriceLessThan5AndHigherThan40()
                .forEach(b -> System.out.printf("%s - $%s%n", b.getTitle(), b.getPrice()));
    }

    private void getAllBookTitleForGoldEditionLessThan5000copies() {
        this.bookService.getAllBooksLessThan5000Copies().forEach(b -> System.out.println(b.getTitle()));
    }

    private void getBookTitlesByAgeRestriction() throws IOException {
        System.out.println("Write ageRestriction:");
        this.bookService.getAllBooksWithCurrentAgeRestriction(bufferedReader.readLine())
                    .stream().map(Book::getTitle).forEach(System.out::println);
    }
}
