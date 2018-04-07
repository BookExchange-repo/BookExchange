package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.StatusEst;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusEstRepository extends CrudRepository<StatusEst, Integer> {
    @Override
    List<StatusEst> findAll();
}
