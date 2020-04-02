package com.example.xmlproductshop.models.dtos.Query4;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDataDto {

    @XmlAttribute(name = "first-name")
    private String firstName;
    @XmlAttribute(name = "last-name")
    private String lastName;
    @XmlAttribute(name = "age")
    private int age;
    @XmlElement(name = "sold-products")
    private SoldProductDto soldProductDto;


    public UserDataDto() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SoldProductDto getSoldProductDto() {
        return soldProductDto;
    }

    public void setSoldProductDto(SoldProductDto soldProductDto) {
        this.soldProductDto = soldProductDto;
    }
}
