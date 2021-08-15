package com.agoda.booking.tracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class CustomerSummaryResponse {
    List<CustomerSummary> results;
}