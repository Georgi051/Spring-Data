package hiberspring.domain.dtos.SeedData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeedRootDto {

    @XmlElement(name = "employee")
    private List<EmployeeSeedDto> employeeSeedDtos;

    public EmployeeSeedRootDto() {
    }

    public List<EmployeeSeedDto> getEmployeeSeedDtos() {
        return employeeSeedDtos;
    }

    public void setEmployeeSeedDtos(List<EmployeeSeedDto> employeeSeedDtos) {
        this.employeeSeedDtos = employeeSeedDtos;
    }
}
