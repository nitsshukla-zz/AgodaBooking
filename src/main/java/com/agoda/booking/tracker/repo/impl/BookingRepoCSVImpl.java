package com.agoda.booking.tracker.repo.impl;

import com.agoda.booking.tracker.model.Booking;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.wrapper.CSVWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

//@Component
public class BookingRepoCSVImpl implements BookingRepo {
  public static final String BOOKING_DATA_CSV = "BookingData.csv";
  private final List<Booking> bookingList;
  public BookingRepoCSVImpl() throws IOException {
    bookingList = CSVWrapper.read(BOOKING_DATA_CSV, Booking.class);
    //Let's not start the application if CSV file is not found.
    //Alternatively, we can assume no booking-data and start app
  }
  @Override
  public List<Booking> getBookingByCustomerId(String customerId) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public List<Booking> findByCustomerIdIn(Collection<String> customerIds) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public List<Booking> findByHotelId(BigInteger hotelId) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public <S extends Booking> S save(S entity) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public Optional<Booking> findById(BigInteger bigInteger) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public boolean existsById(BigInteger bigInteger) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public Iterable<Booking> findAll() {
    return bookingList;
  }

  @Override
  public Iterable<Booking> findAllById(Iterable<BigInteger> bigIntegers) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(BigInteger bigInteger) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public void delete(Booking entity) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public void deleteAllById(Iterable<? extends BigInteger> bigIntegers) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public void deleteAll(Iterable<? extends Booking> entities) {
    throw new IllegalStateException("Not implemented.");
  }

  @Override
  public void deleteAll() {
    throw new IllegalStateException("Not implemented.");
  }

  public <T extends Booking> Iterable<T> saveAll(Iterable<T> bookingListIterable) {
    Iterator<T> bookingListIterator = bookingListIterable.iterator();
    List<Booking> processed = CSVWrapper.write(BOOKING_DATA_CSV, Booking.class, (Iterator<Booking>) bookingListIterator);
    while (bookingListIterator.hasNext())
      bookingList.add(bookingListIterator.next());
    return (Iterable<T>) processed;
  }
}
