package hiberspring.domain.dtos.SeedData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductsSeedRootDto {

    @XmlElement(name = "product")
    private List<ProductsSeedDto> productList;

    public ProductsSeedRootDto() {
    }

    public List<ProductsSeedDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductsSeedDto> productList) {
        this.productList = productList;
    }
}
