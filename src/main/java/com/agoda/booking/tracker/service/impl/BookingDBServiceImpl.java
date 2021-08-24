package com.agoda.booking.tracker.service.impl;

import com.agoda.booking.tracker.dtos.BookingRequest;
import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import com.agoda.booking.tracker.exception.DuplicateBookingPostedException;
import com.agoda.booking.tracker.helper.BookingHelper;
import com.agoda.booking.tracker.helper.CollectionHelper;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static com.agoda.booking.tracker.config.ServiceConfig.MODE_DB;
import static java.math.BigDecimal.ZERO;

@Service(value = MODE_DB)
@RequiredArgsConstructor
public class BookingDBServiceImpl implements BookingService {

  private final BookingRepo bookingDAORepo;
  @Override
  public CustomerSummaryResponse getCustomerSummary(CustomersInfo customersInfo) {
    return new CustomerSummaryResponse(
        bookingDAORepo
            .findByCustomerIdIn(customersInfo.getCustomerIds())
            .stream()
            .collect(Collectors.groupingBy(Booking::getCustomerId))
            .entrySet().stream()
            .map(BookingHelper::summarizeCustomerBookings)
            .collect(Collectors.toList()));
  }

  @Override
  public HotelBookingSummary getHotelBookingSummary(BigInteger hotelId, BigDecimal currentToUSDExchangeRate) {
    BigDecimal totalBooking = bookingDAORepo.findByHotelId(hotelId)
        .stream()
        .map(CONVERT_TO_EXCHANGE_RATE.apply(currentToUSDExchangeRate))
        .reduce(ZERO, BigDecimal::add);
    return new HotelBookingSummary(hotelId, totalBooking);
  }

  @Override
  public HotelBookingSummaryResponse postHotelBookingSummary(BookingRequestList bookingRequestList) {
    checkDupId(bookingRequestList.getBookingRequests());

    List<Booking> bookingList = bookingRequestList
        .getBookingRequests()
        .stream()
        .map(BOOKING_MAPPER)
        .collect(Collectors.toList());
    Iterable<Booking> processedCountIterable = bookingDAORepo.saveAll(bookingList);

    return new HotelBookingSummaryResponse(CollectionHelper.sizeOf(processedCountIterable.iterator()));
  }

  private void checkDupId(List<BookingRequest> bookingRequests) {
    List<Booking> bookings = bookingDAORepo.findByBookingIdIn(bookingRequests.stream().map(BookingRequest::getBookingId).collect(Collectors.toSet()));
    if (!bookings.isEmpty())
      throw new DuplicateBookingPostedException(bookings);
  }

}
