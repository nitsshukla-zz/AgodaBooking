package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CustomersInfo {
  Set<String> customerIds;

  public boolean contains(String customerId) {
    return customerIds.contains(customerId);
  }
}