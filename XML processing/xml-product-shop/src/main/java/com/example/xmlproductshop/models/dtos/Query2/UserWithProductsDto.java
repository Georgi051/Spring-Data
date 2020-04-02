package com.example.xmlproductshop.models.dtos.Query2;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserWithProductsDto {

    @XmlAttribute(name = "first-name")
    private String firstName;

    @XmlAttribute(name = "last-name")
    private String lastName;

    @XmlElement(name = "sold-products")
    private List<SoldProductsRootDto> soldProducts;


    public UserWithProductsDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<SoldProductsRootDto> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<SoldProductsRootDto> soldProducts) {
        this.soldProducts = soldProducts;
    }
}
