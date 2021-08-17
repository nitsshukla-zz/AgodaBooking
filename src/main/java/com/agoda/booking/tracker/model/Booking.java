package com.agoda.booking.tracker.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Booking {
  @CsvBindByName(column = "booking_id")
  private BigInteger bookingId;
  @CsvBindByName(column = "hotel_id")
  private BigInteger hotelId;
  @CsvBindByName(column = "customer_id")
  private String customerId; //can we put UUID as data-type?
  @CsvBindByName(column = "selling_price_local_currency")
  private BigDecimal sellingPriceLocalCurrency;
  @CsvBindByName(column = "to_usd_exchange_rate")
  private BigDecimal exchangeRateInUSD;
  @CsvBindByName(column = "currency")
  private String currency;
  public BigDecimal fetchSellingPriceInUSD() {
    return sellingPriceLocalCurrency.multiply(exchangeRateInUSD);
  }
  public BigDecimal fetchSellingPriceInUSD(@Nullable BigDecimal currentExchangeRate) {
    if (currentExchangeRate == null)
      return fetchSellingPriceInUSD();
    return sellingPriceLocalCurrency.multiply(currentExchangeRate);
  }
}
