package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.Rating;
import softuni.exam.models.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByFirstNameAndLastNameAndEmailAndRating(String fName, String lName, String email, Rating rating);
    Seller findFirstById(Long id);
}
