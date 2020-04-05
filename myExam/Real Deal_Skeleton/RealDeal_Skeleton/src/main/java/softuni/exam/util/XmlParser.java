package softuni.exam.util;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface XmlParser {

    <T> T unmarshalFromFile(String filePath, Class<T> tClass) throws JAXBException, IOException;

    <T> void marshalToFile(String filePath, T rootDto) throws JAXBException;
}
