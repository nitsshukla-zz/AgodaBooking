package com.agoda.booking.tracker.repo.impl;

import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.wrapper.CSVReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Component
public class BookingRepoCSVImpl implements BookingRepo {
  private List<Booking> bookingList;
  public BookingRepoCSVImpl() throws IOException {
    bookingList = CSVReader.read("BookingData.csv", Booking.class);
    //Let's not start the application if CSV file is not found.
    //Alternatively, we can assume no booking-data and start app
  }
  @Override
  public List<Booking> getBookingByCustomerId(String customerId) {
    throw new IllegalStateException("Not impl");
  }

  @Override
  public List<Booking> getAll() {
    return bookingList;
  }
}
