package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Data
public class CustomerSummary {
    //add validations
    String customerID;
    int numberOfBookings;
    BigDecimal totalPriceInUSD;
}