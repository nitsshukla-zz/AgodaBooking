package com.agoda.booking.tracker;


import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import com.agoda.booking.tracker.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

public class BookingServiceTest {
  //"hotel_id","booking_id","customer_id","selling_price_local_currency","currency","to_usd_exchange_rate"
  //"1000134","1000000","4e9a8620-7791-4125-be33-7229ff441be0","939","SGD","0.76"
  private static final String CUST_1 = "4e9a8620-7791-4125-be33-7229ff441be0";
  private static final BigInteger HOTEL_1 = BigInteger.valueOf(1000134);
  private static final Booking BOOKING_1 = new Booking();
  public static final CustomersInfo customersInfo = new CustomersInfo(Collections.singleton(CUST_1));

  static {
    BOOKING_1.setCustomerId(CUST_1);
    BOOKING_1.setBookingId(BigInteger.valueOf(1000000L));
    BOOKING_1.setSellingPriceLocalCurrency(valueOf(1000L));
    BOOKING_1.setExchangeRateInUSD(valueOf(0.5));
    BOOKING_1.setHotelId(HOTEL_1);
  }
  private BookingService bookingService;
  @Mock private BookingRepo bookingRepo;
  @BeforeEach
  public void setup() {
    openMocks(this);
    bookingService = new BookingServiceImpl(bookingRepo);
  }

  @Test
  public void test_getCustomerSummary() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).getAll();
    CustomerSummaryResponse customersSummaryResponse = bookingService.getCustomerSummary(customersInfo);

    assertEquals(1, customersSummaryResponse.getResults().size());
    CustomerSummary customerSummary = customersSummaryResponse.getResults().get(0);
    assertCustomerSummary(customerSummary, CUST_1, 1, BOOKING_1.fetchSellingPriceInUSD());

    verify(bookingRepo).getAll();
  }

  @Test
  public void test_getHotelBookingSummary() {
    BigDecimal exchangeRate = valueOf(2D);
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).getAll();
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), exchangeRate);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(exchangeRate), BOOKING_1.getHotelId());
    verify(bookingRepo).getAll();
  }

  @Test
  public void test_getHotelBookingSummary_noExchangeRate() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).getAll();
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), null);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(), BOOKING_1.getHotelId());
    verify(bookingRepo).getAll();
  }

  private void assertCustomerSummary(CustomerSummary customerSummary, String custId, int numberOfBookings, BigDecimal totalPriceInUSD) {
    assertEquals(custId, customerSummary.getCustomerID());
    assertEquals(numberOfBookings, customerSummary.getNumberOfBookings());
    assertEquals((totalPriceInUSD), customerSummary.getTotalPriceInUSD());
  }

  private void assertHotelSummary(HotelBookingSummary hotelBookingSummary, BigDecimal totalPriceInUSDExpected, BigInteger hotelID) {
    assertEquals(hotelID, hotelBookingSummary.getHotelId());
    assertEquals(totalPriceInUSDExpected, hotelBookingSummary.getTotalPriceInUSD());
  }

}
