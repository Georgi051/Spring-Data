package mostwanted.domain.dtos.races;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "race")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceImportDto {

    @XmlElement
    @NotNull
    private Integer laps;
    @XmlElement(name = "district-name")
    private String districtName;
    @XmlElement(name = "entries")
    private EntryImportRootDto entryImportRootDto;

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public EntryImportRootDto getEntryImportRootDto() {
        return entryImportRootDto;
    }

    public void setEntryImportRootDto(EntryImportRootDto entryImportRootDto) {
        this.entryImportRootDto = entryImportRootDto;
    }
}
