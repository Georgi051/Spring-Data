package hiberspring.domain.dtos.SeedData;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class EmployeeCardsSeedDto {

    @Expose
    @NotNull
    private String number;

    public EmployeeCardsSeedDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
