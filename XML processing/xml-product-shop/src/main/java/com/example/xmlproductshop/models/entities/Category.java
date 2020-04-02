package com.example.xmlproductshop.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity{
    private String name;
    private Set<Product> products;

    public Category() {
    }

    @Column(name = "name")
    @Length(min = 3,max = 15,message = "Category length is not valid!")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToMany(targetEntity = Product.class,mappedBy = "categories")
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
