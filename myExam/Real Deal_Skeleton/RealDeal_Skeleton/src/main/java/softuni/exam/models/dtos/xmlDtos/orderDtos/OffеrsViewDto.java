package softuni.exam.models.dtos.xmlDtos.orderDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OffеrsViewDto {

    @XmlElement(name = "offer")
    private List<OfferDto> offerList;

    public OffеrsViewDto() {
    }

    public List<OfferDto> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<OfferDto> offerList) {
        this.offerList = offerList;
    }
}
