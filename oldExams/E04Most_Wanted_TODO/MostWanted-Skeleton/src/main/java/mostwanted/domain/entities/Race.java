package mostwanted.domain.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "races")
public class Race extends BaseEntity{
    private Integer laps;
    private District district;
    private Set<RaceEntry> entries;

    public Race() {
    }

    @Column(nullable = false,columnDefinition = "integer default 0")
    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @OneToMany(mappedBy = "racer",cascade = CascadeType.ALL)
    public Set<RaceEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<RaceEntry> entries) {
        this.entries = entries;
    }
}
