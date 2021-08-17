package com.agoda.booking.tracker.service;

import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface BookingService {
  CustomerSummaryResponse getCustomerSummary(CustomersInfo customersInfo);

  HotelBookingSummary getHotelBookingSummary(BigInteger hotelId, BigDecimal currentToUSDExchangeRate);
}
