package mostwanted.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class TownImportDto {

    @Expose
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
