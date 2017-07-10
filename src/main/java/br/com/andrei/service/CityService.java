package br.com.andrei.service;

import java.util.List;
import java.util.Optional;

import br.com.andrei.entity.City;
import br.com.andrei.entity.Country;

public interface CityService {

	public List<City> getCities();
	
	public List<City> getCitiesWithCountryId(Country country);

	public City saveCity(City city);
	
	public boolean isCityExist(City city);

	public Optional<City> getCity(Long id);
	
	public City getCityByName(String cityName);

	public void deleteCity(Long id);
}
