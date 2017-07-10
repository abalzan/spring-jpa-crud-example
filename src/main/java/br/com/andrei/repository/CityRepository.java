package br.com.andrei.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.andrei.entity.City;
import br.com.andrei.entity.Country;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	List<City> findByCountry(Country country);
	City findByName (String cityName);
	
}
