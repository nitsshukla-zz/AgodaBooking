package com.agoda.booking.tracker.repo;

import com.agoda.booking.tracker.model.Booking;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Repository
@Primary
public interface BookingRepo extends CrudRepository<Booking, BigInteger> {
  List<Booking> getBookingByCustomerId(String customerId);

  List<Booking> findByCustomerIdIn(Collection<String> customerIds);

  List<Booking> findByHotelId(BigInteger hotelId);
}
