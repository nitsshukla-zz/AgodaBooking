package com.agoda.booking.tracker.service.impl;

import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import com.agoda.booking.tracker.helper.BookingHelper;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.agoda.booking.tracker.config.ServiceConfig.MODE_CSV;
import static com.agoda.booking.tracker.helper.CollectionHelper.sizeOf;
import static com.agoda.booking.tracker.repo.impl.BookingRepoCSVImpl.CSV_REPO;
import static java.math.BigDecimal.ZERO;

@RequiredArgsConstructor
@Service(value = MODE_CSV)
public class BookingCSVServiceImpl implements BookingService {

  @Autowired
  @Qualifier(CSV_REPO)
  private final BookingRepo bookingRepo;

  @Override
  public CustomerSummaryResponse getCustomerSummary(CustomersInfo customersInfo) {
    return new CustomerSummaryResponse(
        StreamSupport.stream(bookingRepo.findAll().spliterator(), true)
          .filter(booking -> customersInfo.contains(booking.getCustomerId()))
          .collect(Collectors.groupingBy(Booking::getCustomerId))
          .entrySet().stream()
          .map(BookingHelper::summarizeCustomerBookings)
          .collect(Collectors.toList())
    );
  }

  @Override
  public HotelBookingSummary getHotelBookingSummary(BigInteger hotelId, BigDecimal currentToUSDExchangeRate) {
    BigDecimal totalBooking = StreamSupport
        .stream(bookingRepo.findAll().spliterator(), true)
        .filter(COMPARE_HOTEL_ID.apply(hotelId))
        .map(CONVERT_TO_EXCHANGE_RATE.apply(currentToUSDExchangeRate))
        .reduce(ZERO, BigDecimal::add);
    return new HotelBookingSummary(hotelId, totalBooking);
  }

  //TODO: remove coupling
  @Override
  public HotelBookingSummaryResponse postHotelBookingSummary(BookingRequestList bookingRequestList) {
    List<Booking> bookingList = bookingRequestList
        .getBookingRequests()
        .stream()
        .map(BOOKING_MAPPER)
        .collect(Collectors.toList());
    Iterable<Booking> iterable = bookingRepo.saveAll(bookingList);
    int processedCount = sizeOf(iterable.iterator());
    return new HotelBookingSummaryResponse(processedCount);
  }
}
