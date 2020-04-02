package com.example.jsonprocessing.models.dtos.fourQuery;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserAndProductsCountsDto {
    @Expose
    private Integer usersCount;
    @Expose
    List<UsersDto> users;

    public UserAndProductsCountsDto() {
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public List<UsersDto> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDto> users) {
        this.users = users;
    }
}
