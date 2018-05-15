package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.*;
import ee.ttu.BookExchange.api.services.*;
import ee.ttu.BookExchange.utilities.SizeHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://bookmarket.online"})
@RequestMapping(produces = "application/json")
public class ClassifierController {
    private CityService cityService;
    private ConditionService conditionService;
    private GenreService genreService;
    private LanguageService languageService;
    private BooksService booksService;
    private StatusService statusService;

    public ClassifierController(BooksService booksService,
                                CityService cityService,
                                ConditionService conditionService,
                                GenreService genreService,
                                LanguageService languageService,
                                StatusService statusService)
    {
        this.booksService = booksService;
        this.cityService = cityService;
        this.conditionService = conditionService;
        this.genreService = genreService;
        this.languageService = languageService;
        this.statusService = statusService;
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
            case "status":
                return Integer.toString(book.getStatus().getId());
            default:
                return null;
        }
    }

    private int getClassifierCount(String classifier, String classifierValue) {
        return booksService.getAllBooks().stream()
                .filter(e -> e.getStatus().getId() == 1)
                .filter(e -> getTableFieldByString(e, classifier).equals(classifierValue))
                .collect(Collectors.toList()).size();
    }

    @RequestMapping(value = "/api/cities/getall", method = RequestMethod.GET)
    public List<City> getAllCities() {
        List<City> allCities = cityService.getAllCities();
        for (City city : allCities) {
            city.setCounter(getClassifierCount("city",
                    Integer.toString(city.getId())));
            city.setMarkerSize(SizeHelper.mapMarkerSizeToString(city.getCounter()));
        }
        allCities = allCities.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getCounter(), e1.getCounter()))
                .collect(Collectors.toList());
        return allCities;
    }

    @RequestMapping(value = "/api/conditions/getall0", method = RequestMethod.GET)
    public List<ConditionEng> getAllConditionsEng() {
        List<ConditionEng> allConditions = conditionService.getAllConditionsEng();
        for (ConditionEng condition : allConditions) {
            condition.setCounter(getClassifierCount("conditiondesc",
                    Integer.toString(condition.getId())));
        }
        return allConditions;
    }

    @RequestMapping(value = "/api/conditions/getall1", method = RequestMethod.GET)
    public List<ConditionEst> getAllConditionsEst() {
        List<ConditionEst> allConditions = conditionService.getAllConditionsEst();
        for (ConditionEst condition : allConditions) {
            condition.setCounter(getClassifierCount("conditiondesc",
                    Integer.toString(condition.getId())));
        }
        return allConditions;
    }

    @RequestMapping(value = "/api/genres/getall0", method = RequestMethod.GET)
    public List<GenreEng> getAllGenresEng() {
        List<GenreEng> allGenres = genreService.getAllGenresEng();
        for (GenreEng genre : allGenres) {
            genre.setCounter(getClassifierCount("genreid",
                    Integer.toString(genre.getId())));
        }
        allGenres = allGenres.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getCounter(), e1.getCounter()))
                .collect(Collectors.toList());
        return allGenres;
    }

    @RequestMapping(value = "/api/genres/getall1", method = RequestMethod.GET)
    public List<GenreEst> getAllGenresEst() {
        List<GenreEst> allGenres = genreService.getAllGenresEst();
        for (GenreEst genre : allGenres) {
            genre.setCounter(getClassifierCount("genreid",
                    Integer.toString(genre.getId())));
        }
        allGenres = allGenres.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getCounter(), e1.getCounter()))
                .collect(Collectors.toList());
        return allGenres;
    }

    @RequestMapping(value = "/api/languages/getall0", method = RequestMethod.GET)
    public List<LanguageEng> getAllLanguagesEng() {
        List<LanguageEng> allLanguages = languageService.getAllLanguagesEng();
        for (LanguageEng language : allLanguages) {
            language.setCounter(getClassifierCount("language",
                    Integer.toString(language.getId())));
        }
        allLanguages = allLanguages.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getCounter(), e1.getCounter()))
                .collect(Collectors.toList());
        return allLanguages;
    }

    @RequestMapping(value = "/api/languages/getall1", method = RequestMethod.GET)
    public List<LanguageEst> getAllLanguagesEst() {
        List<LanguageEst> allLanguages = languageService.getAllLanguagesEst();
        for (LanguageEst language : allLanguages) {
            language.setCounter(getClassifierCount("language",
                    Integer.toString(language.getId())));
        }
        allLanguages = allLanguages.stream()
                .sorted((e1, e2) -> Integer.compare(e2.getCounter(), e1.getCounter()))
                .collect(Collectors.toList());
        return allLanguages;
    }

    @RequestMapping(value = "/api/statuses/getall0", method = RequestMethod.GET)
    public List<StatusEng> getAllStatusesEng() {
        List<StatusEng> allStatuses = statusService.getAllStatusesEng();
        for (StatusEng status : allStatuses) {
            status.setCounter(getClassifierCount("status",
                    Integer.toString(status.getId())));
        }
        return allStatuses;
    }

    @RequestMapping(value = "/api/statuses/getall1", method = RequestMethod.GET)
    public List<StatusEst> getAllStatusesEst() {
        List<StatusEst> allStatuses = statusService.getAllStatusesEst();
        for (StatusEst status : allStatuses) {
            status.setCounter(getClassifierCount("status",
                    Integer.toString(status.getId())));
        }
        return allStatuses;
    }
}
