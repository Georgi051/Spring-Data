package com.example.xmlproductshop.models.dtos.Query4;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProductDto {

    @XmlAttribute
    private int count;

    @XmlElement(name = "product")
    private List<ProductViewDto> productsInfo;

    public SoldProductDto() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ProductViewDto> getProductsInfo() {
        return productsInfo;
    }

    public void setProductsInfo(List<ProductViewDto> productsInfo) {
        this.productsInfo = productsInfo;
    }
}
