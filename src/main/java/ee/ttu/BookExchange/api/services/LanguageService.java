package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.LanguageEng;
import ee.ttu.BookExchange.api.models.LanguageEst;
import ee.ttu.BookExchange.api.repositories.LanguageEngRepository;
import ee.ttu.BookExchange.api.repositories.LanguageEstRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private LanguageEngRepository engRepository;
    private LanguageEstRepository estRepository;

    public LanguageService(LanguageEngRepository engRepository, LanguageEstRepository estRepository) {
        this.engRepository = engRepository;
        this.estRepository = estRepository;
    }

    public List<LanguageEng> getAllLanguagesEng() {
        return engRepository.findAll();
    }

    public List<LanguageEst> getAllLanguagesEst() {
        return estRepository.findAll();
    }
}
