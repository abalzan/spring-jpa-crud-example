package br.com.andrei.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andrei.entity.Country;
import br.com.andrei.repository.CountryRepository;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Override
	public List<Country> getCountries() {
		return countryRepository.findAll();
	}

	@Override
	public void saveCountry(Country country) {
		countryRepository.save(country);
	}

	@Override
	public Optional<Country> getCountry(Long id) {
		return countryRepository.findById(id);
	}

	@Override
	public void deleteCountry(Long id) {
		countryRepository.deleteById(id);
	}

	@Override
	public Country getCountryByName(String countryName) {
		return countryRepository.findByNameContaining(countryName);
	}
	
	@Override
	public boolean isCountryExist(Country country) {
		return countryRepository.findByName(country.getName()) != null;
	}

	@Override
	public void deleteAllCountries() {
		countryRepository.deleteAll();
	}

}