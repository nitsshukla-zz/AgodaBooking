package com.agoda.booking.tracker.service.impl;

import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
  private final static Function<BigInteger, Predicate<Booking>> COMPARE_HOTEL_ID =
      hotelId -> booking -> booking.getHotelId().equals(new BigInteger(hotelId + ""));
  private final static Function<BigDecimal, Function<Booking, BigDecimal>> CONVERT_TO_EXCHANGE_RATE =
      currentToUSDExchangeRate -> booking -> booking.fetchSellingPriceInUSD(currentToUSDExchangeRate);

  @Autowired private final BookingRepo bookingRepo;
  @Override
  public CustomerSummaryResponse getCustomerSummary(CustomersInfo customersInfo) {
    return new CustomerSummaryResponse(
        bookingRepo
          .getAll().parallelStream()
          .filter(booking -> customersInfo.contains(booking.getCustomerId()))
          .collect(Collectors.groupingBy(Booking::getCustomerId))
          .entrySet().stream()
          .map(BookingServiceImpl::summarizeCustomerBookings)
          .collect(Collectors.toList())
    );
  }

  private static CustomerSummary summarizeCustomerBookings(Map.Entry<String, List<Booking>> customerBookingEntries) {
    List<Booking> bookings = customerBookingEntries.getValue();
    BigDecimal totalSell = bookings
        .stream()
        .map(Booking::fetchSellingPriceInUSD)
        .reduce(ZERO, BigDecimal::add);
    return new CustomerSummary(customerBookingEntries.getKey(), bookings.size(), totalSell);
  }

  @Override
  public HotelBookingSummary getHotelBookingSummary(BigInteger hotelId, BigDecimal currentToUSDExchangeRate) {
    BigDecimal totalBooking = bookingRepo.getAll()
        .parallelStream()
        .filter(COMPARE_HOTEL_ID.apply(hotelId))
        .map(CONVERT_TO_EXCHANGE_RATE.apply(currentToUSDExchangeRate))
        .reduce(ZERO, BigDecimal::add);
    return new HotelBookingSummary(hotelId, totalBooking);
  }
}
