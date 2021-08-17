package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class CustomersInfo {
  Set<String> customerIds;

  public boolean contains(String customerId) {
    return customerIds.contains(customerId);
  }
}