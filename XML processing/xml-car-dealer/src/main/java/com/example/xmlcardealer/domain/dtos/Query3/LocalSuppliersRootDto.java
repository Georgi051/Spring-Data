package com.example.xmlcardealer.domain.dtos.Query3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "suppliers")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalSuppliersRootDto {

    @XmlElement(name = "supplier")
    private List<LocalSuppliersDto> supplierList;

    public LocalSuppliersRootDto() {
    }

    public List<LocalSuppliersDto> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<LocalSuppliersDto> supplierList) {
        this.supplierList = supplierList;
    }
}
