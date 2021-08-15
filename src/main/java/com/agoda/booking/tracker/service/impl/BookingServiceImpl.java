package com.agoda.booking.tracker.service.impl;

import com.agoda.booking.tracker.dtos.CustomerInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.repo.BookingRepo;
import com.agoda.booking.tracker.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
  //@Autowired private final BookingRepo bookingRepo;
  @Override
  public CustomerSummaryResponse getCustomerSummary(CustomerInfo customerInfo) {
    return null;
  }

  @Override
  public HotelBookingSummary getHotelBookingSummary(int hotelId, double currentToUSDExchangeRate) {
    return null;
  }
}
