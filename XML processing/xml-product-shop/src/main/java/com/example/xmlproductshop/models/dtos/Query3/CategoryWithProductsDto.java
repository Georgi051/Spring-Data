package com.example.xmlproductshop.models.dtos.Query3;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryWithProductsDto {

    @XmlAttribute(name = "name")
    private String categoryName;
    @XmlElement(name = "products-count")
    private int productsCount;
    @XmlElement(name = "average-price")
    private BigDecimal averagePrice;
    @XmlElement(name = "total-revenue")
    private BigDecimal totalRevue;

    public CategoryWithProductsDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevue() {
        return totalRevue;
    }

    public void setTotalRevue(BigDecimal totalRevue) {
        this.totalRevue = totalRevue;
    }
}
