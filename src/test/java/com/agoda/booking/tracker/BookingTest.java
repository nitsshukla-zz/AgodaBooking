package com.agoda.booking.tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@ParameterizedTest
	@ValueSource(strings = {"abc", "!!"})
	void onGetHotelBookingSummary_wrongHotelId(String hotelId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<?> response = this.restTemplate.exchange(getURL(hotelId, "90"), HttpMethod.GET,
				request,
				Map.class);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	private String getURL(String hotelId, String exchangeRate) {
		return "http://localhost:" + port + "/v1/bookings/hotel/" + hotelId +
				"?current_to_usd_exchange_rate=" + exchangeRate;
	}

	@ParameterizedTest
	@ValueSource(strings = {"abc", "!!"}) //TODO: dup argument with onGetHotelBookingSummary_wrongHotelId
	void onGetHotelBookingSummary_wrongExchangeRate(String exchangeRate) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		HttpEntity<?> request = new HttpEntity<>(headers);

		ResponseEntity<?> response = this.restTemplate.exchange(getURL("1", exchangeRate), HttpMethod.GET,
				request,
				Map.class);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

}
