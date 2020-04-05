package mostwanted.web.controllers;

import mostwanted.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Controller
@RequestMapping("/import")
public class ImportController extends BaseController {

    private final TownService townService;
    private final DistrictService districtService;
    private final RacerService racerService;
    private final CarService carService;
    private final RaceEntryService raceEntryService;
    private final RaceService raceService;

    @Autowired
    public ImportController(TownService townService, DistrictService districtService, RacerService racerService, CarService carService, RaceEntryService raceEntryService, RaceService raceService) {
        this.townService = townService;
        this.districtService = districtService;
        this.racerService = racerService;
        this.carService = carService;
        this.raceEntryService = raceEntryService;
        this.raceService = raceService;
    }

    @GetMapping("/json")
    public ModelAndView importJson() {
        boolean[] areImported = new boolean[]{
                this.townService.townsAreImported(),
                this.districtService.districtsAreImported(),
                this.racerService.racersAreImported(),
                this.carService.carsAreImported()
        };

        return super.view("json/import-json", "areImported", areImported);
    }

    @GetMapping("/xml")
    public ModelAndView importXml() {
        boolean[] areImported = new boolean[]{
                this.raceEntryService.raceEntriesAreImported(),
                this.raceService.racesAreImported()
        };

        return super.view("xml/import-xml", "areImported", areImported);
    }

    @GetMapping("/towns")
    public ModelAndView importTowns() throws IOException {
        return super.view("json/import-towns", "towns", this.townService.readTownsJsonFile());
    }

    @PostMapping("/towns")
    public ModelAndView importTownsConfirm(@RequestParam("towns") String towns) {
        System.out.println(this.townService.importTowns(towns));

        return super.redirect("/import/json");
    }

    @GetMapping("/districts")
    public ModelAndView importDistricts() throws IOException {
        return super.view("json/import-districts", "districts", this.districtService.readDistrictsJsonFile());
    }

    @PostMapping("/districts")
    public ModelAndView importDistrictsConfirm(@RequestParam("districts") String districts) throws IOException {
        System.out.println(this.districtService.importDistricts(districts));

        return super.redirect("/import/json");
    }

    @GetMapping("/racers")
    public ModelAndView importRacers() throws IOException {
        return super.view("json/import-racers", "racers", this.racerService.readRacersJsonFile());
    }

    @PostMapping("/racers")
    public ModelAndView importRacersConfirm(@RequestParam("racers") String racers) {
        System.out.println(this.racerService.importRacers(racers));

        return super.redirect("/import/json");
    }

    @GetMapping("/cars")
    public ModelAndView importCars() throws IOException {
        return super.view("json/import-cars", "cars", this.carService.readCarsJsonFile());
    }

    @PostMapping("/cars")
    public ModelAndView importCarsConfirm(@RequestParam("cars") String cars) throws IOException {
        System.out.println(this.carService.importCars(cars));

        return super.redirect("/import/json");
    }

    @GetMapping("/race-entries")
    public ModelAndView importRaceEntries() throws IOException {
        return super.view("xml/import-race-entries", "raceEntries", this.raceEntryService.readRaceEntriesXmlFile());
    }

    @PostMapping("/race-entries")
    public ModelAndView importRaceEntriesConfirm() throws JAXBException, IOException {
        System.out.println(this.raceEntryService.importRaceEntries());

        return super.redirect("/import/xml");
    }

    @GetMapping("/races")
    public ModelAndView importRaces() throws IOException {
        return super.view("xml/import-races", "races", this.raceService.readRacesXmlFile());
    }

    @PostMapping("/races")
    public ModelAndView importRacesConfirm() throws JAXBException, IOException {
        System.out.println(this.raceService.importRaces());

        return super.redirect("/import/xml");
    }
}
