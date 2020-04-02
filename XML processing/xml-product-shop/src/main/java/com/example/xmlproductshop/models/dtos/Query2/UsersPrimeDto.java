package com.example.xmlproductshop.models.dtos.Query2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersPrimeDto {

    @XmlElement(name = "user")
    private List<UserWithProductsDto> users;

    public UsersPrimeDto() {
    }

    public List<UserWithProductsDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserWithProductsDto> users) {
        this.users = users;
    }
}
