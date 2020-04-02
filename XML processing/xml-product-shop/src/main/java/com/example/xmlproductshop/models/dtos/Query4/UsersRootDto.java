package com.example.xmlproductshop.models.dtos.Query4;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersRootDto {

    @XmlAttribute(name = "count")
    private int count;

    @XmlElement(name = "user")
    private List<UserDataDto> userInfo;

    public UsersRootDto() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserDataDto> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserDataDto> userInfo) {
        this.userInfo = userInfo;
    }
}
