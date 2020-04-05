package mostwanted.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "racers")
public class Racer extends BaseEntity{

    private String name;
    private Integer age;
    private BigDecimal bounty;
    private Town town;
    private List<Car> cars;

    public Racer() {
    }


    @Column(nullable = false,unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column
    public BigDecimal getBounty() {
        return bounty;
    }

    public void setBounty(BigDecimal bounty) {
        this.bounty = bounty;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    @OneToMany(mappedBy = "racer",cascade = CascadeType.ALL)
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
