package com.agoda.booking.tracker.controller;

import com.agoda.booking.tracker.config.ServiceConfig;
import com.agoda.booking.tracker.dtos.BookingRequestList;
import com.agoda.booking.tracker.dtos.CustomersInfo;
import com.agoda.booking.tracker.dtos.CustomerSummaryResponse;
import com.agoda.booking.tracker.dtos.HotelBookingSummary;
import com.agoda.booking.tracker.dtos.HotelBookingSummaryResponse;
import com.agoda.booking.tracker.service.BookingService;
import com.google.gson.annotations.SerializedName;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/v1/bookings")
@Timed(histogram = true)
public class BookingController {
  private final BookingService bookingService;

  @Autowired
  public BookingController(ServiceConfig.ServiceFactory serviceFactory) {
    bookingService = serviceFactory.get();
  }

  @ApiOperation(value = "Upload Bookings in JSON/XML format")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Bookings inserted successfully"),
          @ApiResponse(code = 400, message = "Validation failed"),
          @ApiResponse(code = 422, message = "Same Booking-ID exception"),
          @ApiResponse(code = 500, message = "Internal error")
      }
  )
  @PostMapping(
      consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
      produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public HotelBookingSummaryResponse postHotelBookingSummary(@RequestBody @Valid BookingRequestList bookingRequestList) {
    return bookingService.postHotelBookingSummary(bookingRequestList);
  }

  @ApiOperation(value = "Query bookings for a hotel")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Bookings queried successfully"),
          @ApiResponse(code = 400, message = "Validation failed"),
          @ApiResponse(code = 500, message = "Internal error")
      }
  )
  @GetMapping("/hotel/{hotelId}")
  @ResponseBody
  //TODO: Put authorization
  public HotelBookingSummary getHotelBookingSummary(
      @PathVariable("hotelId") @Valid BigInteger hotelId,
      @RequestParam(value = "current_to_usd_exchange_rate", required = false) BigDecimal currentToUSDExchangeRate) {
    return bookingService.getHotelBookingSummary(hotelId, currentToUSDExchangeRate);
  }

  @ApiOperation(value = "Query customer summary")
  @ApiResponses(
      value = {
          @ApiResponse(code = 200, message = "Customer summary queried successfully"),
          @ApiResponse(code = 400, message = "Validation failed"),
          @ApiResponse(code = 500, message = "Internal error")
      }
  )
  @PostMapping("/customer/summary")
  public CustomerSummaryResponse getCustomerSummary(@RequestBody CustomersInfo customersInfo) {
    return bookingService.getCustomerSummary(customersInfo);
  }
}
