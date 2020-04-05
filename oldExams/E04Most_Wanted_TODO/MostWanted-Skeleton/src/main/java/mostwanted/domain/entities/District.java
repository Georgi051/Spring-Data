package mostwanted.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "districts")
public class District  extends  BaseEntity{

    private String name;
    private Town town;


    public District() {
    }

    @Column(nullable = false,unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(targetEntity = Town.class,cascade = CascadeType.ALL)
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
