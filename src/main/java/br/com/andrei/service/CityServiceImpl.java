package br.com.andrei.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andrei.entity.City;
import br.com.andrei.entity.Country;
import br.com.andrei.repository.CityRepository;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityRepository cityRepository;

	@Override
	public List<City> getCities() {
		return cityRepository.findAll();
	}

	@Override
	public City saveCity(City city) {
		return cityRepository.save(city);
	}

	@Override
	public Optional<City> getCity(Long id) {
		return cityRepository.findById(id);
	}

	@Override
	public void deleteCity(Long id) {
		cityRepository.deleteById(id);
	}

	@Override
	public List<City> getCitiesWithCountryId(Country country) {
		return cityRepository.findByCountry(country);
	}

	@Override
	public boolean isCityExist(City city) {
		return cityRepository.findById(city.getId()).isPresent();
	}

	@Override
	public City getCityByName(String cityName) {
		return cityRepository.findByName(cityName);
	}

}
