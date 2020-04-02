package com.example.xmlcardealer.domain.dtos.Query2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToyotaRootDto {

    @XmlElement(name = "car")
    private List<ToyotaCarsDto> cars;

    public ToyotaRootDto() {
    }

    public List<ToyotaCarsDto> getCars() {
        return cars;
    }

    public void setCars(List<ToyotaCarsDto> cars) {
        this.cars = cars;
    }
}
