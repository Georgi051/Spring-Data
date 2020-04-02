package com.example.xmlcardealer.domain.dtos.Query6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleRootDto {

    @XmlElement(name = "sale")
    private List<CustomerInfoDto> customersList;

    public SaleRootDto() {
    }

    public List<CustomerInfoDto> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<CustomerInfoDto> customersList) {
        this.customersList = customersList;
    }
}
