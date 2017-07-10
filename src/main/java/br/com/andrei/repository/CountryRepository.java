package br.com.andrei.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.andrei.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>{

	Country findByNameContaining(String name);
	Country findByName(String name);
	
}
