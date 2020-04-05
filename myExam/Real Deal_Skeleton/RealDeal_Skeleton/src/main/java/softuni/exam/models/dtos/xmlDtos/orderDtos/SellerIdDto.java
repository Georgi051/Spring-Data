package softuni.exam.models.dtos.xmlDtos.orderDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "saller")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerIdDto {

    @XmlElement
    private Long id;

    public SellerIdDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
