package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.CityService;
import ee.ttu.BookExchange.api.services.ConditionService;
import ee.ttu.BookExchange.api.services.GenreService;
import ee.ttu.BookExchange.api.services.LanguageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.Condition;

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
        List<City> allCities = cityService.getAllCities();
        return allCities;
    }

    @RequestMapping(value = "/api/conditions/getall0", method = RequestMethod.GET)
    public List<ConditionEng> getAllConditionsEng() {
        List<ConditionEng> allConditions = conditionService.getAllConditionsEng();
        return allConditions;
    }

    @RequestMapping(value = "/api/conditions/getall1", method = RequestMethod.GET)
    public List<ConditionEst> getAllConditionsEst() {
        List<ConditionEst> allConditions = conditionService.getAllConditionsEst();
        return allConditions;
    }

    @RequestMapping(value = "/api/genres/getall0", method = RequestMethod.GET)
    public List<GenreEng> getAllGenresEng() {
        List<GenreEng> allGenres = genreService.getAllGenresEng();
        return allGenres;
    }

    @RequestMapping(value = "/api/genres/getall1", method = RequestMethod.GET)
    public List<GenreEst> getAllGenresEst() {
        List<GenreEst> allGenres = genreService.getAllGenresEst();
        return allGenres;
    }

    @RequestMapping(value = "/api/languages/getall0", method = RequestMethod.GET)
    public List<LanguageEng> getAllLanguagesEng() {
        List<LanguageEng> allLanguages = languageService.getAllLanguagesEng();
        return allLanguages;
    }

    @RequestMapping(value = "/api/languages/getall1", method = RequestMethod.GET)
    public List<LanguageEst> getAllLanguagesEst() {
        List<LanguageEst> allLanguages = languageService.getAllLanguagesEst();
        return allLanguages;
    }
}
