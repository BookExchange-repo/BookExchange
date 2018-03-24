package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.CityService;
import ee.ttu.BookExchange.api.services.ConditionService;
import ee.ttu.BookExchange.api.services.GenreService;
import ee.ttu.BookExchange.api.services.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(produces = "application/json")
public class ClassifierController {
    private CityService cityService;
    private ConditionService conditionService;
    private GenreService genreService;
    private LanguageService languageService;

    public ClassifierController(CityService cityService,
                                ConditionService conditionService,
                                GenreService genreService,
                                LanguageService languageService)
    {
        this.cityService = cityService;
        this.conditionService = conditionService;
        this.genreService = genreService;
        this.languageService = languageService;
    }

    @RequestMapping(value = "/api/cities/getall", method = RequestMethod.GET)
    public List<City> getAllCities() {
        return cityService.getAllCities();
    }

    @RequestMapping(value = "/api/conditions/getall0", method = RequestMethod.GET)
    public List<ConditionEng> getAllConditionsEng() {
        return conditionService.getAllConditionsEng();
    }

    @RequestMapping(value = "/api/conditions/getall1", method = RequestMethod.GET)
    public List<ConditionEst> getAllConditionsEst() {
        return conditionService.getAllConditionsEst();
    }

    @RequestMapping(value = "/api/genres/getall0", method = RequestMethod.GET)
    public List<GenreEng> getAllGenresEng() {
        return genreService.getAllGenresEng();
    }

    @RequestMapping(value = "/api/genres/getall1", method = RequestMethod.GET)
    public List<GenreEst> getAllGenresEst() {
        return genreService.getAllGenresEst();
    }

    @RequestMapping(value = "/api/languages/getall0", method = RequestMethod.GET)
    public List<LanguageEng> getAllLanguagesEng() {
        return languageService.getAllLanguagesEng();
    }

    @RequestMapping(value = "/api/languages/getall1", method = RequestMethod.GET)
    public List<LanguageEst> getAllLanguagesEst() {
        return languageService.getAllLanguagesEst();
    }
}
