package com.example.xmlcardealer.domain.dtos.Query1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomersRootDto {

    @XmlElement(name = "customer")
    private List<CustomersDto> customers;

    public CustomersRootDto() {
    }

    public List<CustomersDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomersDto> customers) {
        this.customers = customers;
    }
}
