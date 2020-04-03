package softuni.exam.domain.dtos.pictureSeed;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class PictureSeedViewDto {

    @XmlElement(name = "picture")
    private List<PictureSeedDto> pictures;

    public PictureSeedViewDto() {
    }

    @NotNull
    public List<PictureSeedDto> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureSeedDto> pictures) {
        this.pictures = pictures;
    }
}
