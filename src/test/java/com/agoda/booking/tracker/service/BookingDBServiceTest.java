package com.agoda.booking.tracker.service;


import com.agoda.booking.tracker.dtos.BookingRequest;
import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.helper.CollectionHelper;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.impl.BookingDBServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.agoda.booking.tracker.service.BookingService.BOOKING_MAPPER;
import static com.agoda.booking.tracker.service.BookingService.BOOKING_REQ_MAPPER;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class BookingDBServiceTest extends BookingServiceTest {
  private BookingService bookingService;
  @Mock private BookingRepo bookingRepo;
  @BeforeEach
  public void setup() {
    openMocks(this);
    bookingService = new BookingDBServiceImpl(bookingRepo);
  }

  @Test
  public void test_getCustomerSummary() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findByCustomerIdIn(customersInfo.getCustomerIds());
    CustomerSummaryResponse customersSummaryResponse = bookingService.getCustomerSummary(customersInfo);

    assertEquals(1, customersSummaryResponse.getResults().size());
    CustomerSummary customerSummary = customersSummaryResponse.getResults().get(0);
    assertCustomerSummary(customerSummary, CUST_1, 1, BOOKING_1.fetchSellingPriceInUSD());

    verify(bookingRepo).findByCustomerIdIn(customersInfo.getCustomerIds());
  }

  @Test
  public void test_getHotelBookingSummary() {
    BigDecimal exchangeRate = valueOf(2D);
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findByHotelId(HOTEL_1);
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), exchangeRate);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(exchangeRate), BOOKING_1.getHotelId());
    verify(bookingRepo).findByHotelId(HOTEL_1);
  }

  @Test
  public void test_getHotelBookingSummary_noExchangeRate() {
    doReturn(singletonList(BOOKING_1)).when(bookingRepo).findByHotelId(HOTEL_1);
    HotelBookingSummary hotelBookingSummary = bookingService.getHotelBookingSummary(BOOKING_1.getHotelId(), null);
    assertHotelSummary(hotelBookingSummary, BOOKING_1.fetchSellingPriceInUSD(), BOOKING_1.getHotelId());
    verify(bookingRepo).findByHotelId(HOTEL_1);
  }

  @Test
  public void test_postData() {
    List<BookingRequest> bookingRequestList = singletonList(BOOKING_REQ_MAPPER.apply(BOOKING_1));
    BookingRequestList bookings = new BookingRequestList();
    bookings.setBookingRequests(bookingRequestList);
    bookingService.postHotelBookingSummary(bookings);
    ArgumentCaptor<Iterable<Booking>> captor = ArgumentCaptor.forClass(Iterable.class);
    verify(bookingRepo).saveAll(captor.capture());
    assertBooking(bookingRequestList, captor.getValue());
  }

  protected void assertBooking(List<BookingRequest> bookingRequests, Iterable<Booking> bookings) {
    assertEquals(bookingRequests.size(), CollectionHelper.sizeOf(bookings.iterator()));
    List<Booking> expectedBooking= bookingRequests
        .stream()
        .map(BOOKING_MAPPER)
        .sorted()
        .collect(Collectors.toList());
    assertBookingsEquals(expectedBooking.iterator(), bookings.iterator());
  }

  private void assertBookingsEquals(Iterator<Booking> expectedBookings, Iterator<Booking> bookings) {
    while(expectedBookings.hasNext() && bookings.hasNext()) {
      Booking expectedBooking = expectedBookings.next(), actualBooking = bookings.next();
      assertEquals(expectedBooking.toString(), actualBooking.toString());
    }
  }

}
