package entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;


@Entity
@Table(name = "gp")
public class Gp extends BaseEntity {
    private Set<History> histories;

    public Gp() {
    }

    @OneToMany(targetEntity = History.class)
    @JoinColumn(name = "gp_id", referencedColumnName = "id")
    public Set<History> getPatients() {
        return histories;
    }

    public void setPatients(Set<History> histories) {
        this.histories = histories;
    }
}
