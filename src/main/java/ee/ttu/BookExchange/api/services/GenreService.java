package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.GenreEng;
import ee.ttu.BookExchange.api.models.GenreEst;
import ee.ttu.BookExchange.api.models.StatusEng;
import ee.ttu.BookExchange.api.models.StatusEst;
import ee.ttu.BookExchange.api.repositories.GenreEngRepository;
import ee.ttu.BookExchange.api.repositories.GenreEstRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private GenreEngRepository engRepository;
    private GenreEstRepository estRepository;

    public GenreService(GenreEngRepository engRepository, GenreEstRepository estRepository) {
        this.engRepository = engRepository;
        this.estRepository = estRepository;
    }

    public List<GenreEng> getAllGenresEng() {
        return engRepository.findAll();
    }

    public List<GenreEst> getAllGenresEst() {
        return estRepository.findAll();
    }

    public GenreEng getGenreByIdEng(int genreId) {
        return engRepository.findOne(genreId);
    }

    public GenreEst getGenreByIdEst(int genreId) {
        return estRepository.findOne(genreId);
    }
}
