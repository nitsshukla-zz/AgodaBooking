package com.agoda.booking.tracker.controller;

import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.service.BookingService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("/v1/bookings")
public class BookingController {
  @Autowired private BookingService bookingService;

  @GetMapping("/hotel/{hotelId}")
  //Put prometheus, swagger, authorization
  public HotelBookingSummary getHotelBookingSummary(
      @PathVariable("hotelId") @Valid BigInteger hotelId,
      @RequestParam(value = "current_to_usd_exchange_rate", required = false) BigDecimal currentToUSDExchangeRate) {
    return bookingService.getHotelBookingSummary(hotelId, currentToUSDExchangeRate);
  }
  @PostMapping("/customer/summary")
  public CustomerSummaryResponse getCustomerSummary(@RequestBody CustomersInfo customersInfo) {
    return bookingService.getCustomerSummary(customersInfo);
  }

}
