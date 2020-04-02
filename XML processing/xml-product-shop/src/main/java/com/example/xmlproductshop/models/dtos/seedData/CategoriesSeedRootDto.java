package com.example.xmlproductshop.models.dtos.seedData;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoriesSeedRootDto {

    @XmlElement(name = "category")
    private List<CategorySeedDto> categoryList;

    public CategoriesSeedRootDto() {
    }

    public List<CategorySeedDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategorySeedDto> categoryList) {
        this.categoryList = categoryList;
    }
}
