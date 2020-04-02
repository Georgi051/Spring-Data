package com.example.xmlproductshop.models.dtos.Query3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriesRootDto {

    @XmlElement(name = "category")
    private List<CategoryWithProductsDto> categoryList;

    public CategoriesRootDto() {
    }

    public List<CategoryWithProductsDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryWithProductsDto> categoryList) {
        this.categoryList = categoryList;
    }
}
