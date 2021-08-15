package com.agoda.booking.tracker.service;

import com.agoda.booking.tracker.dtos.CustomerInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;

public interface BookingService {
  CustomerSummaryResponse getCustomerSummary(CustomerInfo customerInfo);

  HotelBookingSummary getHotelBookingSummary(int hotelId, double currentToUSDExchangeRate);
}
