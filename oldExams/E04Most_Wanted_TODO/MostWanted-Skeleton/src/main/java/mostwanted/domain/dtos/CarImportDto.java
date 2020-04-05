package mostwanted.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CarImportDto {

    @Expose
    @NotNull
    private String brand;
    @Expose
    @NotNull
    private String model;
    @Expose
    private BigDecimal price;
    @Expose
    @NotNull
    private Integer yearOfProduction;
    @Expose
    private Double 	maxSpeed;
    @Expose
    private Double	zeroToSixty;
    @Expose
    private String racerName;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Double getZeroToSixty() {
        return zeroToSixty;
    }

    public void setZeroToSixty(Double zeroToSixty) {
        this.zeroToSixty = zeroToSixty;
    }

    public String getRacerName() {
        return racerName;
    }

    public void setRacerName(String racerName) {
        this.racerName = racerName;
    }
}
