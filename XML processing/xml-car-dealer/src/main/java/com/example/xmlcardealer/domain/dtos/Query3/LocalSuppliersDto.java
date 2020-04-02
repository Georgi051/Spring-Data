package com.example.xmlcardealer.domain.dtos.Query3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "suplier")
@XmlAccessorType(XmlAccessType.FIELD)
public class LocalSuppliersDto {

    @XmlAttribute
    private Long id;
    @XmlAttribute
    private String name;
    @XmlAttribute(name = "parts-count")
    private int partsCount;

    public LocalSuppliersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPartsCount() {
        return partsCount;
    }

    public void setPartsCount(int partsCount) {
        this.partsCount = partsCount;
    }
}
