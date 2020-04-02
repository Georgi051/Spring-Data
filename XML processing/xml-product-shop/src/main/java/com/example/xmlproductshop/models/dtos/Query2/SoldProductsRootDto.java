package com.example.xmlproductshop.models.dtos.Query2;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoldProductsRootDto {

    @XmlElement(name = "product")
    private List<ProductInfoDto> productsList;

    public SoldProductsRootDto() {
    }

    public List<ProductInfoDto> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<ProductInfoDto> productsList) {
        this.productsList = productsList;
    }
}
