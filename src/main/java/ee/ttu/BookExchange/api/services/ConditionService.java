package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.ConditionEng;
import ee.ttu.BookExchange.api.models.ConditionEst;
import ee.ttu.BookExchange.api.repositories.ConditionEngRepository;
import ee.ttu.BookExchange.api.repositories.ConditionEstRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {
    private ConditionEngRepository engRepository;
    private ConditionEstRepository estRepository;

    public ConditionService(ConditionEngRepository engRepository, ConditionEstRepository estRepository) {
        this.engRepository = engRepository;
        this.estRepository = estRepository;
    }

    public List<ConditionEng> getAllConditionsEng() {
        return engRepository.findAll();
    }

    public List<ConditionEst> getAllConditionsEst() {
        return estRepository.findAll();
    }
}
