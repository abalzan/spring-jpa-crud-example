package br.com.andrei.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.andrei.entity.City;
import br.com.andrei.entity.Country;
import br.com.andrei.service.CityService;
import br.com.andrei.service.CountryService;

@SpringBootTest(classes = { RestCityController.class })
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(secure = false)
public class RestCityControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CityService cityService;

	@MockBean
	private CountryService countryService;

	@Test
	public void getCitiesTestSuccess() throws Exception {

		List<City> cities = Arrays.asList(new City(5L, "New York", new Country(2L, "United States")),
				new City(6L, "Los Angeles", new Country(2L, "United States")),
				new City(7L, "Atlanta", new Country(2L, "United States")));

		Mockito.when(countryService.getCountryByName(Mockito.anyString())).thenReturn(new Country(2L, "United States"));

		Mockito.when(cityService.getCitiesWithCountryId(Mockito.any(Country.class))).thenReturn(cities);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cities?country=Uni")
				.accept(MediaType.APPLICATION_JSON_VALUE);

		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		String expected = "[{name:New York,country:{name:United States,id:2},id:5},"
				+ "{name:Los Angeles,country:{name:United States,id:2},id:6},"
				+ "{name:Atlanta,country:{name:United States,id:2},id:7}]";

		String result = mvcResult.getResponse().getContentAsString().replaceAll("\"", "");

		assertEquals(expected, result);

	}

	@Test
	public void getCityTestSucess() throws Exception {
		Mockito.when(cityService.getCity(Mockito.anyLong()))
				.thenReturn(Optional.of(new City(1L, "Curitiba", new Country(1L, "Brazil"))));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cities/get/1")
				.accept(MediaType.APPLICATION_JSON_VALUE);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{name:Curitiba,country:{name:Brazil,id:1},id:1}";

		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

	}

	@Test
	public void getCityTestNullValue() throws Exception {

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/cities/get/100")
				.accept(MediaType.APPLICATION_JSON_VALUE);

		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

		String expected = "{errorMessage:No City found with the id 100.}";

		String result = mvcResult.getResponse().getContentAsString().replaceAll("\"", "");

		assertEquals(expected, result);

	}

	@Test
	public void addCitySuccessTest() throws Exception {
		City city = new City(99L, "Seara", new Country(1L, "Brasil"));
		
		MvcResult mvcResult = this.mockMvc.perform(post("/cities/add").content(asJsonString(city))
					.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String expectedMessage = "City successfully created";
		
		assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
		
	}
	
	@Test
	public void updateCitySuccessTest() throws Exception{
		City city = new City(10L, "Natal", new Country(1L, "Brasil"));
		
		Mockito.when(cityService.getCity(Mockito.anyLong()))
		.thenReturn(Optional.of(new City(10L, "Chapeco", new Country(1L, "Brazil"))));
		
		MvcResult mvcResult = this.mockMvc.perform(put("/cities/update/"+city.getId()+"?name="+city.getName()).content(asJsonString(city))
					.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String expectedMessage = "City successfully updated";
		
		assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
	}
	
	@Test
	public void deleteSuccessTest() throws Exception{
		City city = new City(10L, "Chapeco", new Country(1L, "Brasil"));
		
		Mockito.when(cityService.getCity(Mockito.anyLong()))
		.thenReturn(Optional.of(new City(10L, "Chapeco", new Country(1L, "Brazil"))));

		MvcResult mvcResult = this.mockMvc.perform(delete("/cities/delete/"+city.getId())
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		String expectedMessage = "City successfully deleted 10";
		
		assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	} 
}
