package mostwanted.repository;

import mostwanted.domain.entities.RaceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceEntryRepository  extends JpaRepository<RaceEntry,Integer> {
    //TODO: Implement me
}
