package com.example.xmlproductshop.models.entities;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private Integer age;
    private String firstName;
    private String lastName;
    private Set<User> friends;
    private Set<Product> productBuys;
    private Set<Product> productSells;

    public User() {
    }


    @Column(name = "age")
    @Min(value = 14,message = "User must be at least 14 age to shop!")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name",nullable = false)
    @Size(min = 3,message = "Last name must be at least three symbols!")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToMany()
    @JoinTable(name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }


    @OneToMany(mappedBy = "buyer", targetEntity = Product.class, fetch = FetchType.EAGER)
    public Set<Product> getProductBuys() {
        return productBuys;
    }

    public void setProductBuys(Set<Product> productBuys) {
        this.productBuys = productBuys;
    }

    @OneToMany(mappedBy = "seller", targetEntity = Product.class, fetch = FetchType.EAGER)
    public Set<Product> getProductSells() {
        return productSells;
    }

    public void setProductSells(Set<Product> productSells) {
        this.productSells = productSells;
    }
}
