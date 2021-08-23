package com.agoda.booking.tracker.helper;

import com.agoda.booking.tracker.dtos.CustomerSummary;
import com.agoda.booking.tracker.model.Booking;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.ZERO;

public class BookingHelper {
  public static CustomerSummary summarizeCustomerBookings(Map.Entry<String, List<Booking>> customerBookingEntries) {
    List<Booking> bookings = customerBookingEntries.getValue();
    BigDecimal totalSell = bookings
        .stream()
        .map(Booking::fetchSellingPriceInUSD)
        .reduce(ZERO, BigDecimal::add);
    return new CustomerSummary(customerBookingEntries.getKey(), bookings.size(), totalSell);
  }
}
