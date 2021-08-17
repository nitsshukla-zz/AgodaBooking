package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CustomerSummary {
    //add validations
    String customerID;
    int numberOfBookings;
    BigDecimal totalPriceInUSD;
}