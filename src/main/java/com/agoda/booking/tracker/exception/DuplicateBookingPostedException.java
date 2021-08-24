package com.agoda.booking.tracker.exception;

import com.agoda.booking.tracker.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class DuplicateBookingPostedException extends RuntimeException {
  public DuplicateBookingPostedException(List<Booking> bookings) {
    super("IDs already found: " + bookings.stream().map(Booking::getBookingId).collect(Collectors.toSet()));
  }
}
