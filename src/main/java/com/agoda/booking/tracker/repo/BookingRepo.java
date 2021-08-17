package com.agoda.booking.tracker.repo;

import com.agoda.booking.tracker.model.Booking;

import java.util.List;

public interface BookingRepo {
  List<Booking> getBookingByCustomerId(String customerId);

  List<Booking> getAll();
}
