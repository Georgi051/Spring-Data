package mostwanted.web.controllers;

import mostwanted.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private final TownService townService;
    private final DistrictService districtService;
    private final RacerService racerService;
    private final CarService carService;
    private final RaceEntryService raceEntryService;
    private final RaceService raceService;

    @Autowired
    public HomeController(TownService townService, DistrictService districtService, RacerService racerService, CarService carService, RaceEntryService raceEntryService, RaceService raceService) {
        this.townService = townService;
        this.districtService = districtService;
        this.racerService = racerService;
        this.carService = carService;
        this.raceEntryService = raceEntryService;
        this.raceService = raceService;
    }

    @GetMapping("")
    public ModelAndView index() {
        boolean areImported = this.townService.townsAreImported() &&
                this.districtService.districtsAreImported() &&
                this.racerService.racersAreImported() &&
                this.carService.carsAreImported() &&
                this.raceEntryService.raceEntriesAreImported() &&
                this.raceService.racesAreImported();

        return super.view("index", "areImported", areImported);
    }
}
