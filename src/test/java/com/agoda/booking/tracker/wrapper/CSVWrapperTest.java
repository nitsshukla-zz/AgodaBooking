package com.agoda.booking.tracker.wrapper;

import com.agoda.booking.tracker.model.Booking;
import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;
import org.junit.jupiter.api.Test;

import javax.validation.ValidatorFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.agoda.booking.tracker.service.BookingServiceTest.BOOKING_1;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVWrapperTest {
  @Test
  public void test_wrapperRead() throws IOException {
    List<Booking> bookingList = CSVWrapper.read("hotelBooking.csv", Booking.class);
    assertNotNull(bookingList);
    assertEquals(3, bookingList.size());
  }

  @Test
  public void test_wrapperWrite_FileNotFound() throws IOException {
    assertThrows(FileNotFoundException.class,
        () -> CSVWrapper
        .write("hotelBooking1.csv",
            Booking.class,
            singletonList(BOOKING_1).iterator()
        ));
  }

  @Test
  public void test_wrapperWrite() throws IOException {
    List<Booking> bookingList = CSVWrapper
        .write("hotelBooking_write.csv",
            Booking.class,
            singletonList(BOOKING_1).iterator()
            );
    assertNotNull(bookingList);
    assertEquals(1, bookingList.size());
  }
}
