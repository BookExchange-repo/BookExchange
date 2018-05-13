package ee.ttu.BookExchange.api.controllers;

import ee.ttu.BookExchange.api.models.Books;
import ee.ttu.BookExchange.api.models.City;
import ee.ttu.BookExchange.api.models.GenreEst;
import ee.ttu.BookExchange.api.services.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://bookmarket.online"})
@RequestMapping(value = "/api/stats", produces = "application/json")
public class StatsController {
    private BooksService booksService;

    public StatsController(BooksService booksService) {
        this.booksService = booksService;
    }

    @RequestMapping(value = "main", method = RequestMethod.GET)
    Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();
        List<String> allErrors = new ArrayList<>();

        List<Books> allBooks = booksService.getAllBooks();
        int totalBooks = allBooks.size();
        result.put("totalBooks", totalBooks);

        BigDecimal averageBookPrice = new BigDecimal(0);
        for (Books book : allBooks) {
            averageBookPrice = averageBookPrice.add(book.getPrice());
        }
        averageBookPrice = averageBookPrice.divide(new BigDecimal(totalBooks), 2, RoundingMode.HALF_UP);
        result.put("averageBookPrice", averageBookPrice);

        Map<GenreEst, Long> allGenreOccurences = allBooks.stream()
                .map(e -> e.getGenreid())
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        Comparator<? super Map.Entry<GenreEst, Long>> maxValueComparatorGenre =
                (e1, e2) -> e1.getValue().compareTo(e2.getValue());
        Map.Entry<GenreEst, Long> mostFrequentGenreMap =
                allGenreOccurences.entrySet().stream().max(maxValueComparatorGenre).get();
        mostFrequentGenreMap.getKey().setCounter(mostFrequentGenreMap.getValue().intValue());
        result.put("mostPopularGenre", mostFrequentGenreMap.getKey());

        Map<City, Long> allCityOccurences = allBooks.stream()
                .map(e -> e.getCity())
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        Comparator<? super Map.Entry<City, Long>> maxValueComparatorCity =
                (e1, e2) -> e1.getValue().compareTo(e2.getValue());
        Map.Entry<City, Long> mostFrequentCityMap =
                allCityOccurences.entrySet().stream().max(maxValueComparatorCity).get();
        mostFrequentCityMap.getKey().setCounter(mostFrequentCityMap.getValue().intValue());
        result.put("mostPopularCity", mostFrequentCityMap.getKey());

        return result;
    }

    @RequestMapping(value = "recent", method = RequestMethod.GET)
    List<Books> getRecentBooks(@RequestParam(value = "amount") Optional<Integer> recentAmount) {
        if (!recentAmount.isPresent())
            recentAmount = Optional.of(5);

        List<Books> lastBooksDesc = booksService.getAllBooks();
        Collections.reverse(lastBooksDesc);
        lastBooksDesc = lastBooksDesc.stream()
                .filter(e -> !e.getImagepath().equals("https://bookmarket.online:18000/images/no-image.svg"))
                .limit(recentAmount.get())
                .collect(Collectors.toList());

        return lastBooksDesc;
    }
}
