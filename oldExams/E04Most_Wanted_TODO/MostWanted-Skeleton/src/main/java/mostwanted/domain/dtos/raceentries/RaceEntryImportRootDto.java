package mostwanted.domain.dtos.raceentries;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "race-entries")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceEntryImportRootDto {

    @XmlElement(name = "race-entry")
    List<RaceEntryImportDto> raceList;

    public List<RaceEntryImportDto> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<RaceEntryImportDto> raceList) {
        this.raceList = raceList;
    }
}