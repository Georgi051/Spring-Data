package mostwanted.domain.dtos.raceentries;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "race-entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class RaceEntryImportDto {

    @XmlAttribute(name = "has-finished")
    private boolean hasFinished;
    @XmlAttribute(name = "finish-time")
    private Double finishTime;
    @XmlAttribute(name = "car-id")
    private Integer id;
    @XmlElement
    @NotNull
    private String racer;


    public boolean isHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(boolean hasFinished) {
        this.hasFinished = hasFinished;
    }

    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRacer() {
        return racer;
    }

    public void setRacer(String racer) {
        this.racer = racer;
    }
}
