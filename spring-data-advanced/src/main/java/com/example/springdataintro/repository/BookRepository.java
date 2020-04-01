package com.example.springdataintro.repository;

import com.example.springdataintro.entities.AgeRestriction;
import com.example.springdataintro.entities.Book;
import com.example.springdataintro.entities.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType,int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal less, BigDecimal greater);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate before,LocalDate after);

    List<Book> findAllByReleaseDateBefore(LocalDate before);

    List<Book> findAllByTitleContains(String letters);

    @Query("SELECT b FROM Book AS b WHERE b.author.lastName LIKE concat(:start ,'%')")
    List<Book> getBooksByAuthorLastNameStartsWith(@Param("start") String start);

    @Query("SELECT count(b) FROM Book AS b WHERE length(b.title) > :num")
    int getBooksWhereTitleIsBiggerThanCurrentNumber(@Param("num") int num);

    @Query("SELECT concat(b.author.firstName,' ',b.author.lastName), sum(b.copies) as copies from  Book as b " +
            "group by b.author.id order by copies desc")
    List<Object[]>getAllAuthorByCopiesInDescOrder();

    Book findByTitle(String title);
}
