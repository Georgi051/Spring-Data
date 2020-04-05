package mostwanted.repository;

import mostwanted.domain.entities.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {
    Town findByName(String name);

    @Query(value = "select r.town.name, COUNT(r.town.id)  from  Racer  as r " +
            "GROUP BY  r.town.id ORDER BY  count(r.town.id) desc, r.town.name asc ")
            List<Object[]>exsportAllTowns();
}
