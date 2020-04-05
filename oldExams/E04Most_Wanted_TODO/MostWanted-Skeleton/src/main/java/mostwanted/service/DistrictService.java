package mostwanted.service;

import mostwanted.domain.entities.District;

import java.io.IOException;

public interface DistrictService {

    Boolean districtsAreImported();

    String readDistrictsJsonFile() throws IOException;

    String importDistricts(String districtsFileContent) throws IOException;

    District findByName(String name);
}
