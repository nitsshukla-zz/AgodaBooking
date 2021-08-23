package com.agoda.booking.tracker.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.agoda.booking.tracker.model.Booking.BOOKING_ID_COLUMN_NAME;
import static com.agoda.booking.tracker.model.Booking.CURRENCY_COLUMN_NAME;
import static com.agoda.booking.tracker.model.Booking.CUSTOMER_ID_COLUMN_NAME;
import static com.agoda.booking.tracker.model.Booking.SELLING_PRICE_LOCAL_CURRENCY_COLUMN_NAME;
import static com.agoda.booking.tracker.model.Booking.TO_USD_EXCHANGE_RATE_COLUMN_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
  @SerializedName(BOOKING_ID_COLUMN_NAME)
  @NotNull
  private BigInteger bookingId;
  @SerializedName(BOOKING_ID_COLUMN_NAME)
  @NotNull
  private BigInteger hotelId;
  @SerializedName(CUSTOMER_ID_COLUMN_NAME)
  @NotEmpty
  @NotNull
  private String customerId; //can we put UUID as data-type?
  @SerializedName(SELLING_PRICE_LOCAL_CURRENCY_COLUMN_NAME)
  @NotNull
  private BigDecimal sellingPriceLocalCurrency;
  @SerializedName(TO_USD_EXCHANGE_RATE_COLUMN_NAME)
  private BigDecimal exchangeRateInUSD;
  @SerializedName(CURRENCY_COLUMN_NAME)
  @NotEmpty
  @NotNull
  private String currency;
}
