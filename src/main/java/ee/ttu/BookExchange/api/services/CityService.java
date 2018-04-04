package ee.ttu.BookExchange.api.services;

import ee.ttu.BookExchange.api.models.City;
import ee.ttu.BookExchange.api.repositories.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(int cityId) {
        return cityRepository.findOne(cityId);
    }
}
