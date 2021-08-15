package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class CustomerSummary {
    //add validations
    String customerID;
    Long numberOfBookings;
    Double totalPriceInUSD;
}