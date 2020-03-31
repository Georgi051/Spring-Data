package entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "history")
public class History extends BaseEntity {
    private Set<Patient> patients;

    public History() {
    }

    @ManyToMany
    @JoinTable(name = "history_patients"
            , joinColumns = @JoinColumn(name = "history_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"))
    public Set<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

}
