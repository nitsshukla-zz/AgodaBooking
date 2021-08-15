package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HotelBookingSummary {

    private final Integer hotelId;
    private final Double totalPriceInUSD;

}