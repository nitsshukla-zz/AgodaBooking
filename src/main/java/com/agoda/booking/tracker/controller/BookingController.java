package com.agoda.booking.tracker.controller;

import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import com.agoda.booking.tracker.service.BookingService;
import com.google.gson.annotations.SerializedName;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import static com.agoda.booking.tracker.model.Booking.*;

@RestController
@RequestMapping("/v1/bookings")
@Timed(histogram = true)
public class BookingController {
  @Autowired private BookingService bookingService;

  @PostMapping(
      consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
      produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public HotelBookingSummaryResponse postHotelBookingSummary(@RequestBody @Valid BookingRequestList bookingRequestList) {
    return bookingService.postHotelBookingSummary(bookingRequestList);
  }
  @GetMapping("/hotel/{hotelId}")
  @ResponseBody
  //TODO: Put prometheus, authorization
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
