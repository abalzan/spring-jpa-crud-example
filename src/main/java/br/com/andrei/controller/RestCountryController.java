package br.com.andrei.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.andrei.entity.Country;
import br.com.andrei.service.CountryService;

@RestController
@EnableWebMvc
@RequestMapping("/country")
public class RestCountryController {


    public static final Logger logger = LoggerFactory.getLogger(RestCountryController.class);
 
    @Autowired
    private CountryService countryService;
 
    //@RequestMapping(value = "/country/", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Country>> listAllCountries() {
        
    	List<Country> countries = countryService.getCountries();
        
        if (countries.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
    }
 
    //find a single country
    @RequestMapping(value = "/country/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getCountry(@PathVariable("id") long id) {
        logger.info("Fetching Country with id {}", id);
        Optional<Country> country = countryService.getCountry(id);
        if (country == null) {
            logger.error("Country with id {} not found.", id);
            return new ResponseEntity<>("Country with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
 
    // -------------------Create a Country-------------------------------------------
    @RequestMapping(value = "/country/", method = RequestMethod.POST)
    public ResponseEntity<?> addCountry(@RequestBody Country country, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Country : {}", country);
 
        if (countryService.isCountryExist(country)) {
            logger.error("Unable to create. A Country with name {} already exist", country.getName());
            return new ResponseEntity<>("Unable to create. A Country with name " + 
            country.getName() + " already exist.",HttpStatus.CONFLICT);
        }
        countryService.saveCountry(country);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/country/{id}").buildAndExpand(country.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
 
    // ------------------- Update a Country ------------------------------------------------
    @RequestMapping(value = "/country/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCountry(@PathVariable("id") long id, @RequestBody Country country) {
        logger.info("Updating Country with id {}", id);
 
        Optional<Country> countryExist = countryService.getCountry(id);
        Country currentCountry = countryExist.get();
 
        if (currentCountry == null) {
            logger.error("Unable to update. Country with id {} not found.", id);
            return new ResponseEntity<>("Unable to upate. Country with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        
        currentCountry.setName(country.getName());
 
        countryService.saveCountry(currentCountry);
        return new ResponseEntity<>(currentCountry, HttpStatus.OK);
    }
 
    // ------------------- Delete a Country-----------------------------------------
    @RequestMapping(value = "/country/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCountry(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Country with id {}", id);
 
        Optional<Country> country = countryService.getCountry(id);
        if (country == null) {
            logger.error("Unable to delete. Country with id {} not found.", id);
            return new ResponseEntity<String>("Unable to delete. Country with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        countryService.deleteCountry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
 
    // ------------------- Delete All Countries-----------------------------
    @RequestMapping(value = "/country/", method = RequestMethod.DELETE)
    public ResponseEntity<Country> deleteAllCountries() {
        logger.info("Deleting All Countries");
        countryService.deleteAllCountries();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
