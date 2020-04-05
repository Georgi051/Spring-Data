package mostwanted.domain.dtos.races;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "entries")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntryImportRootDto {

    @XmlElement(name = "entry")
    private List<EntryImportDto> entityList;

    public List<EntryImportDto> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<EntryImportDto> entityList) {
        this.entityList = entityList;
    }
}
