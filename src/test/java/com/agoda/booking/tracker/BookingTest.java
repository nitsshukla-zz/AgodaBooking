package com.agoda.booking.tracker;

import com.agoda.booking.tracker.dtos.BookingRequest;
import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@ParameterizedTest
	@ValueSource(strings = {"abc", "!!"})
	void onGetHotelBookingSummary_wrongHotelId(String hotelId) {
		ResponseEntity<?> response = this.restTemplate.exchange(getURL(hotelId, "90"), HttpMethod.GET,
				getRequest(),
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
		ResponseEntity<?> response = this.restTemplate.exchange(getURL("1", exchangeRate), HttpMethod.GET,
				getRequest(),
				Map.class);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	private HttpEntity<?> getRequest() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		return new HttpEntity<>(headers);

	}

	@ParameterizedTest
	@CsvFileSource(files = "src/test/resources/static/hotelBooking.csv", numLinesToSkip = 1)
	void onPostHotelBookings(String hotelId, String bookingId, String custId,
																						 String localCurrencySP, String currency, BigDecimal exchangeRateInUSD) {
		ResponseEntity<HotelBookingSummaryResponse> response = this.restTemplate.postForEntity(
				"http://localhost:" + port + "/v1/bookings",
				getBookingData(hotelId, bookingId, custId, localCurrencySP, currency, exchangeRateInUSD),
				HotelBookingSummaryResponse.class
		);
		assertTrue(response.getStatusCode().is2xxSuccessful());
	}

	@ParameterizedTest
	@CsvSource(value = {
			"1000134,1000000,4e9a8620-7791-4125-be33-7229ff441be0,939,null,0.120",
			"1000134,1000000,4e9a8620-7791-4125-be33-7229ff441be0,null,INR,0.120",
			"1000134,1000000,null,939,INR,0.120",
			"1000134,null,4e9a8620-7791-4125-be33-7229ff441be0,939,INR,0.120",
			"null,1000000,4e9a8620-7791-4125-be33-7229ff441be0,939,INR,0.120",
	}, nullValues = "null")
	void onPostHotelBookings_invalidValues(String hotelId, String bookingId, String custId,
																						 String localCurrencySP, String currency, BigDecimal exchangeRateInUSD) {
		ResponseEntity<String> response = this.restTemplate.postForEntity(
				"http://localhost:" + port + "/v1/bookings",
				getBookingData(hotelId, bookingId, custId, localCurrencySP, currency, exchangeRateInUSD),
				String.class
		);
		assertTrue(response.getStatusCode().is4xxClientError());
	}

	private BookingRequestList getBookingData(String hotelId,
																															 String bookingId,
																															 String custId,
																															 String localCurrencySP,
																															 String currency,
																															 BigDecimal exchangeRateInUSD) {
		BookingRequestList bookingRequestList = new BookingRequestList();
		BookingRequest bookingRequest = new BookingRequest(
				bookingId==null?null:new BigInteger(bookingId),
				hotelId==null?null:new BigInteger(hotelId),
				custId,
				localCurrencySP==null?null:new BigDecimal(localCurrencySP),
				exchangeRateInUSD,
				currency
		);
		bookingRequestList.setBookingRequests(Collections.singletonList(bookingRequest));
		return bookingRequestList;
	}


}
