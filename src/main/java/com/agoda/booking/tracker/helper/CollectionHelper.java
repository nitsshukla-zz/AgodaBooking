package com.agoda.booking.tracker.helper;

import com.agoda.booking.tracker.model.Booking;

import java.util.Iterator;

public class CollectionHelper {
  public static int sizeOf(Iterator<Booking> iterator) {
    int size = 0;
    while (iterator.hasNext()) {
      iterator.next();
      size++;
    }
    return size;
  }
}
