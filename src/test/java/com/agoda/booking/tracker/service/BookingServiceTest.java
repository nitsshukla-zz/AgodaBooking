package com.agoda.booking.tracker.service;

import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public abstract class BookingServiceTest {
  //"hotel_id","booking_id","customer_id","selling_price_local_currency","currency","to_usd_exchange_rate"
  //"1000134","1000000","4e9a8620-7791-4125-be33-7229ff441be0","939","SGD","0.76"
  protected static final String CUST_1 = "4e9a8620-7791-4125-be33-7229ff441be0";
  protected static final BigInteger HOTEL_1 = BigInteger.valueOf(1000134);
  public static final Booking BOOKING_1 = new Booking();
  protected static final CustomersInfo customersInfo = new CustomersInfo(Collections.singleton(CUST_1));
  protected BookingService bookingService;
  @Mock
  protected BookingRepo bookingRepo;
  static {
    BOOKING_1.setCustomerId(CUST_1);
    BOOKING_1.setBookingId(BigInteger.valueOf(1000000L));
    BOOKING_1.setSellingPriceLocalCurrency(valueOf(1000L));
    BOOKING_1.setExchangeRateInUSD(valueOf(0.5));
    BOOKING_1.setHotelId(HOTEL_1);
  }

  protected void assertCustomerSummary(CustomerSummary customerSummary, String custId, int numberOfBookings, BigDecimal totalPriceInUSD) {
    assertEquals(custId, customerSummary.getCustomerID());
    assertEquals(numberOfBookings, customerSummary.getNumberOfBookings());
    assertEquals((totalPriceInUSD), customerSummary.getTotalPriceInUSD());
  }

  protected void assertHotelSummary(HotelBookingSummary hotelBookingSummary, BigDecimal totalPriceInUSDExpected, BigInteger hotelID) {
    assertEquals(hotelID, hotelBookingSummary.getHotelId());
    assertEquals(totalPriceInUSDExpected, hotelBookingSummary.getTotalPriceInUSD());
  }

}
