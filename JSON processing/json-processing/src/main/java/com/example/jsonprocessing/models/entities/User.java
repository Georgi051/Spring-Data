package com.example.jsonprocessing.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private int age;
    private Set<User> friends;
    private Set<Product> productBuys;
    private Set<Product> productSells;

    public User() {
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    @Length(min = 3, message = "Wrong length")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
