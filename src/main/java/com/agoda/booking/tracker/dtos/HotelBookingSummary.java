package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@Data @NoArgsConstructor
public class HotelBookingSummary {

    private BigInteger hotelId;
    private BigDecimal totalPriceInUSD;

}