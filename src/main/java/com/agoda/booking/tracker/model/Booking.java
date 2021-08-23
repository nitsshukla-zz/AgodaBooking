package com.agoda.booking.tracker.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "booking", indexes = {
    @Index(name = "hotel_index", columnList = Booking.HOTEL_ID_COLUMN_NAME),
    @Index(name = "cust_index", columnList = Booking.CUSTOMER_ID_COLUMN_NAME),
})
public class Booking {
  public static final String HOTEL_ID_COLUMN_NAME = "hotel_id";
  public static final String BOOKING_ID_COLUMN_NAME = "booking_id";
  public static final String CUSTOMER_ID_COLUMN_NAME = "customer_id";
  public static final String SELLING_PRICE_LOCAL_CURRENCY_COLUMN_NAME = "selling_price_local_currency";
  public static final String TO_USD_EXCHANGE_RATE_COLUMN_NAME = "to_usd_exchange_rate";
  public static final String CURRENCY_COLUMN_NAME = "currency";

  @Id
  @Column(name = BOOKING_ID_COLUMN_NAME)
  @CsvBindByName(column = BOOKING_ID_COLUMN_NAME)
  private BigInteger bookingId;
  @CsvBindByName(column = HOTEL_ID_COLUMN_NAME)
  @Column(name = HOTEL_ID_COLUMN_NAME)
  private BigInteger hotelId;
  @CsvBindByName(column = CUSTOMER_ID_COLUMN_NAME)
  @Column(name = CUSTOMER_ID_COLUMN_NAME)
  private String customerId; //can we put UUID as data-type?
  @CsvBindByName(column = SELLING_PRICE_LOCAL_CURRENCY_COLUMN_NAME)
  @Column(name = SELLING_PRICE_LOCAL_CURRENCY_COLUMN_NAME)
  private BigDecimal sellingPriceLocalCurrency;
  @CsvBindByName(column = TO_USD_EXCHANGE_RATE_COLUMN_NAME)
  @Column(name = TO_USD_EXCHANGE_RATE_COLUMN_NAME)
  private BigDecimal exchangeRateInUSD;
  @CsvBindByName(column = CURRENCY_COLUMN_NAME)
  @Column(name = CURRENCY_COLUMN_NAME)
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
