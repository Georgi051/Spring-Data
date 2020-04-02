package com.example.xmlcardealer.domain.dtos.Query6;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "sale")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarInfoDto {

    @XmlAttribute
    private String make;
    @XmlAttribute
    private String model;
    @XmlAttribute(name = "travelled-distance")
    private Long travelledDistance;


    public CarInfoDto() {
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }
}
