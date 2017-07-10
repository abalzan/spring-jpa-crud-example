package br.com.andrei.service;

import java.util.List;
import java.util.Optional;

import br.com.andrei.entity.Country;

public interface CountryService {
	
	public List<Country> getCountries();

	public void saveCountry(Country county);
	
	public Optional<Country> getCountry(Long id);

	public void deleteCountry(Long id);
	
	public void deleteAllCountries();
	
	public Country getCountryByName(String countryName);
	
	public boolean isCountryExist(Country country);
}
