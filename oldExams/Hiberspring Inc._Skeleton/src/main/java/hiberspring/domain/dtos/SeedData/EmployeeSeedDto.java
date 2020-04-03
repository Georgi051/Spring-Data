package hiberspring.domain.dtos.SeedData;

import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.EmployeeCard;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeedDto {

    @XmlAttribute(name = "first-name")
    @NotNull
    private String firstName;
    @XmlAttribute(name = "last-name")
    @NotNull
    private String lastName;
    @XmlAttribute
    @NotNull
    private String position;
    @XmlElement
    private String card;
    @XmlElement
    private String branch;

    public EmployeeSeedDto() {
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
