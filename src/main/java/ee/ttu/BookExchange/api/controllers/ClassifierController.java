package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(produces = "application/json")
public class ClassifierController {
    private CityService cityService;
    private ConditionService conditionService;
    private GenreService genreService;
    private LanguageService languageService;
    private BooksService booksService;

    public ClassifierController(BooksService booksService,
                                CityService cityService,
                                ConditionService conditionService,
                                GenreService genreService,
                                LanguageService languageService)
    {
        this.booksService = booksService;
        this.cityService = cityService;
        this.conditionService = conditionService;
        this.genreService = genreService;
        this.languageService = languageService;
    }

    private String getTableFieldByString(Books book, String tableField) {
        switch (tableField) {
            case "id":
                return Integer.toString(book.getId());
            case "title":
                return book.getTitle();
            case "author":
                return book.getAuthor();
            case "description":
                return book.getDescription();
            case "conditiondesc":
                return Integer.toString(book.getConditiondesc().getId());
            case "price":
                return book.getPrice().toString();
            case "likes":
                return Integer.toString(book.getLikes());
            case "isbn":
                return book.getIsbn();
            case "imagepath":
                return book.getImagepath();
            case "publisher":
                return book.getPublisher();
            case "pubyear":
                return book.getPubyear();
            case "language":
                return Integer.toString(book.getLanguage().getId());
            case "postdate":
                return book.getPostdate().toString();
            case "userid":
                return Integer.toString(book.getUserid().getId());
            case "genreid":
                return Integer.toString(book.getGenreid().getId());
            case "city":
                return Integer.toString(book.getCity().getId());
            default:
                return null;
        }
    }

    private int getClassifierCount(String classifier, String classifierValue) {
        return booksService.getAllBooks().stream()
                .filter(e -> getTableFieldByString(e, classifier).equals(classifierValue))
                .collect(Collectors.toList()).size();
    }

    @RequestMapping(value = "/api/cities/getall", method = RequestMethod.GET)
    public List<Object> getAllCities() {
        List<Object> allCities = (List<Object>)(List<?>)cityService.getAllCities();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (City city : (List<City>)(List<?>)allCities) {
            countMap.put("count" + city.getId(), getClassifierCount("city",
                    Integer.toString(city.getId())));
        }
        allCities.add(countMap);
        return allCities;
    }

    @RequestMapping(value = "/api/conditions/getall0", method = RequestMethod.GET)
    public List<Object> getAllConditionsEng() {
        List<Object> allConditions = (List<Object>)(List<?>)conditionService.getAllConditionsEng();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (ConditionEng condition : (List<ConditionEng>)(List<?>)allConditions) {
            countMap.put("count" + condition.getId(), getClassifierCount("conditiondesc",
                    Integer.toString(condition.getId())));
        }
        allConditions.add(countMap);
        return allConditions;
    }

    @RequestMapping(value = "/api/conditions/getall1", method = RequestMethod.GET)
    public List<Object> getAllConditionsEst() {
        List<Object> allConditions = (List<Object>)(List<?>)conditionService.getAllConditionsEst();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (ConditionEst condition : (List<ConditionEst>)(List<?>)allConditions) {
            countMap.put("count" + condition.getId(), getClassifierCount("conditiondesc",
                    Integer.toString(condition.getId())));
        }
        allConditions.add(countMap);
        return allConditions;
    }

    @RequestMapping(value = "/api/genres/getall0", method = RequestMethod.GET)
    public List<Object> getAllGenresEng() {
        List<Object> allGenres = (List<Object>)(List<?>)genreService.getAllGenresEng();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (GenreEng genre : (List<GenreEng>)(List<?>)allGenres) {
            countMap.put("count" + genre.getId(), getClassifierCount("genreid",
                    Integer.toString(genre.getId())));
        }
        allGenres.add(countMap);
        return allGenres;
    }

    @RequestMapping(value = "/api/genres/getall1", method = RequestMethod.GET)
    public List<Object> getAllGenresEst() {
        List<Object> allGenres = (List<Object>)(List<?>)genreService.getAllGenresEst();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (GenreEst genre : (List<GenreEst>)(List<?>)allGenres) {
            countMap.put("count" + genre.getId(), getClassifierCount("genreid",
                    Integer.toString(genre.getId())));
        }
        allGenres.add(countMap);
        return allGenres;
    }

    @RequestMapping(value = "/api/languages/getall0", method = RequestMethod.GET)
    public List<Object> getAllLanguagesEng() {
        List<Object> allLanguages = (List<Object>)(List<?>)languageService.getAllLanguagesEng();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (LanguageEng language : (List<LanguageEng>)(List<?>)allLanguages) {
            countMap.put("count" + language.getId(), getClassifierCount("language",
                    Integer.toString(language.getId())));
        }
        allLanguages.add(countMap);
        return allLanguages;
    }

    @RequestMapping(value = "/api/languages/getall1", method = RequestMethod.GET)
    public List<Object> getAllLanguagesEst() {
        List<Object> allLanguages = (List<Object>)(List<?>)languageService.getAllLanguagesEst();
        Map<String, Object> countMap = new HashMap<>();
        countMap.put("counters", true);
        for (LanguageEst language : (List<LanguageEst>)(List<?>)allLanguages) {
            countMap.put("count" + language.getId(), getClassifierCount("language",
                    Integer.toString(language.getId())));
        }
        allLanguages.add(countMap);
        return allLanguages;
    }
}
