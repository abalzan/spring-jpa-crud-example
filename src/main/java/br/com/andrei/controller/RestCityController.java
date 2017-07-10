package br.com.andrei.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.com.andrei.entity.City;
import br.com.andrei.entity.Country;
import br.com.andrei.error.CustomErrorType;
import br.com.andrei.service.CityService;
import br.com.andrei.service.CountryService;

@RestController
@EnableWebMvc
@RequestMapping("/cities")
public class RestCityController {

	@Autowired
	private CityService cityService;
	@Autowired
	private CountryService countryService;

	public static final Logger logger = LoggerFactory.getLogger(RestCountryController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<City>> getCities(@RequestParam("country") String countryName) {

		Country country = countryService.getCountryByName(countryName);
		
		if (country == null) {
			return new ResponseEntity(new CustomErrorType("No Country found with name "+countryName),HttpStatus.NOT_FOUND);
		}
		
		List<City> cities = cityService.getCitiesWithCountryId(country);
		
		if (cities == null || cities.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("No City found with country "+country.getName()),HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<Optional<City>> getCity(@PathVariable("id") Long id) {
		logger.info("Fetching City with id {}", id);
		Optional<City> city = cityService.getCity(id);
		if (!city.isPresent()) {
			logger.error("City with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("No City found with the id "+ id +"."),HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(city, HttpStatus.OK);
	}

	@RequestMapping(value="/add",method = RequestMethod.POST)
	public ResponseEntity<City> addCity(@RequestBody City city) {
		logger.info("Creating City : {}", city);

		if (cityService.isCityExist(city)) {
			logger.error("Unable to create. A City with name {} already exist", city.getName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A City with name "+city.getName()+" already exist"), HttpStatus.CONFLICT);
		}
		cityService.saveCity(city);
		
		return new ResponseEntity("City successfully created", HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<City> updateCity(@PathVariable("id") long id, @RequestParam("name") String cityName ) {
		logger.info("Updating City with id {}",id);
		 
        Optional<City> cityExist = cityService.getCity(id);
        City currentCity = cityExist.get();
 
        if (currentCity == null) {
            logger.error("Unable to update. City with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to update. City with name "+ id +" not found."), HttpStatus.NOT_FOUND);
        }
        
        currentCity.setName(cityName);
 
        cityService.saveCity(currentCity);
        return new ResponseEntity("City successfully updated", HttpStatus.OK);

	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCity(@PathVariable Long id) {
		logger.info("Fetching & Deleting City with id {}", id);
		 		
		Optional<City> city = cityService.getCity(id);

		if (city == null) {
			logger.error("Unable to delete. City with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("No City found for ID " + id), HttpStatus.NOT_FOUND);
		} else {
			cityService.deleteCity(id);
			return new ResponseEntity<>("City successfully deleted " + id, HttpStatus.NO_CONTENT);
		}

	}


}
