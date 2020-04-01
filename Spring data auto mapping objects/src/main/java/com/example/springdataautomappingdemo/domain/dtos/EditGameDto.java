package com.example.springdataautomappingdemo.domain.dtos;
import org.springframework.lang.NonNull;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EditGameDto {
    private Long id;
    private BigDecimal price;
    private double size;
    private String title;
    private String trailer;
    private String image;
    private String description;
    private LocalDate realiseDate;

    public EditGameDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Pattern(regexp = "^[A-Z].{3,100}$",message = "Title does not match the given parameters!")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Size(min = 11,max = 11,message = "Тrailer does not match the given parameters!")
    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    @Pattern(regexp = "(http(s)?:\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)"
            ,message = "Thumbnail does not match the given parameters!")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Min(value = 0,message = "Size does not match the given parameters!")
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }


    @DecimalMin(value = "0",message = "Price does not match the given parameters!")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Size(min = 20)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRealiseDate() {
        return realiseDate;
    }

    public void setRealiseDate(LocalDate realiseDate) {
        this.realiseDate = realiseDate;
    }
}
