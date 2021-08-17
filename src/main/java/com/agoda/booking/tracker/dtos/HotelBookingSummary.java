package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@Data
public class HotelBookingSummary {

    private final BigInteger hotelId;
    private final BigDecimal totalPriceInUSD;

}