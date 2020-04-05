package softuni.exam.models.dtos.xmlDtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerSeedViewDto {

    @XmlElement(name = "seller")
    private List<SellerSeedDto> sellerList;

    public SellerSeedViewDto() {
    }

    public List<SellerSeedDto> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<SellerSeedDto> sellerList) {
        this.sellerList = sellerList;
    }
}
