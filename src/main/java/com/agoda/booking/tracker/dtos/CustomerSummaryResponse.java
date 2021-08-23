package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor @NoArgsConstructor
@Data
public class CustomerSummaryResponse {
    List<CustomerSummary> results;
}