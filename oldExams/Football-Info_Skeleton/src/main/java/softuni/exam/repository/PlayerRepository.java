package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlayerRepository  extends JpaRepository<Player,Integer> {
    Player findByFirstNameAndLastName(String fName,String lName);

    List<Player> findAllByTeamName(String teamName);

    List<Player> getAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal salary);
}
