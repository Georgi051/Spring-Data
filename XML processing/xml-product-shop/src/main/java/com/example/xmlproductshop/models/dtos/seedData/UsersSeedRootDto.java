package com.example.xmlproductshop.models.dtos.seedData;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersSeedRootDto {

    @XmlElement(name = "user")
    private List<UserSeedDto> users;

    public UsersSeedRootDto() {
    }

    public List<UserSeedDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserSeedDto> users) {
        this.users = users;
    }
}
