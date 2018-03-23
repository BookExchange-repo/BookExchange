package ee.ttu.BookExchange.api.repositories;

import ee.ttu.BookExchange.api.models.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {
    @Override
    List<City> findAll();
}
