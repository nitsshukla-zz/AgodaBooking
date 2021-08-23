package com.agoda.booking.tracker.service;


import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import com.agoda.booking.tracker.service.impl.BookingCSVServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class BookingCSVServiceTest extends BookingServiceTest {

  @BeforeEach
  public void setup() {
    openMocks(this);
    bookingService = new BookingCSVServiceImpl(bookingRepo);
  }

  @Test
  public void test_getCustomerSummary() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findAll();
    CustomerSummaryResponse customersSummaryResponse = bookingService.getCustomerSummary(customersInfo);

    assertEquals(1, customersSummaryResponse.getResults().size());
    CustomerSummary customerSummary = customersSummaryResponse.getResults().get(0);
    assertCustomerSummary(customerSummary, CUST_1, 1, BOOKING_1.fetchSellingPriceInUSD());

    verify(bookingRepo).findAll();
  }

  @Test
  public void test_getHotelBookingSummary() {
    BigDecimal exchangeRate = valueOf(2D);
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findAll();
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), exchangeRate);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(exchangeRate), BOOKING_1.getHotelId());
    verify(bookingRepo).findAll();
  }

  @Test
  public void test_getHotelBookingSummary_noExchangeRate() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findAll();
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), null);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(), BOOKING_1.getHotelId());
    verify(bookingRepo).findAll();
  }
}
