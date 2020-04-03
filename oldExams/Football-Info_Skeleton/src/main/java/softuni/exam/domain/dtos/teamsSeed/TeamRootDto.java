package softuni.exam.domain.dtos.teamsSeed;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamRootDto {

    @XmlElement(name = "team")
    private List<TeamDto> teamDtoList;

    public TeamRootDto() {
    }

    public List<TeamDto> getTeamDtoList() {
        return teamDtoList;
    }

    public void setTeamDtoList(List<TeamDto> teamDtoList) {
        this.teamDtoList = teamDtoList;
    }
}
