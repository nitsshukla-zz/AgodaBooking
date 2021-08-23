package com.agoda.booking.tracker.service;

import com.agoda.booking.tracker.dtos.BookingRequest;
import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import com.agoda.booking.tracker.model.Booking;
import org.dozer.DozerBeanMapper;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.BackOffPolicy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

@Retryable(
    maxAttemptsExpression =  "#{${retry.daoMaxAttempts}}",
    backoff = @Backoff(delay = 100),
    include = {
        TransientDataAccessException.class
    }
)
public interface BookingService {
  Function<BigInteger, Predicate<Booking>> COMPARE_HOTEL_ID =
      hotelId -> booking -> booking.getHotelId().equals(new BigInteger(hotelId + ""));
  Function<BigDecimal, Function<Booking, BigDecimal>> CONVERT_TO_EXCHANGE_RATE =
      currentToUSDExchangeRate -> booking -> booking.fetchSellingPriceInUSD(currentToUSDExchangeRate);
  DozerBeanMapper mapper = new DozerBeanMapper();
  Function<BookingRequest, Booking> BOOKING_MAPPER =
      request -> mapper.map(request, Booking.class);
  Function<Booking, BookingRequest> BOOKING_REQ_MAPPER =
      request -> mapper.map(request, BookingRequest.class);

  CustomerSummaryResponse getCustomerSummary(CustomersInfo customersInfo);

  HotelBookingSummary getHotelBookingSummary(BigInteger hotelId, BigDecimal currentToUSDExchangeRate);

  HotelBookingSummaryResponse postHotelBookingSummary(BookingRequestList bookingRequestList);
}
