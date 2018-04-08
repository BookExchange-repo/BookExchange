package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.StatusEng;
import ee.ttu.BookExchange.api.models.StatusEst;
import ee.ttu.BookExchange.api.repositories.StatusEngRepository;
import ee.ttu.BookExchange.api.repositories.StatusEstRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private StatusEngRepository engRepository;
    private StatusEstRepository estRepository;

    public StatusService(StatusEngRepository engRepository, StatusEstRepository estRepository) {
        this.engRepository = engRepository;
        this.estRepository = estRepository;
    }

    public List<StatusEng> getAllStatusesEng() {
        return engRepository.findAll();
    }

    public List<StatusEst> getAllStatusesEst() {
        return estRepository.findAll();
    }

    public StatusEng getStatusByIdEng(int statusId) {
        return engRepository.findOne(statusId);
    }

    public StatusEst getStatusByIdEst(int statusId) {
        return estRepository.findOne(statusId);
    }
}
