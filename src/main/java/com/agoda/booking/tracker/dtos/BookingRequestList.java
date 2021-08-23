package com.agoda.booking.tracker.dtos;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class BookingRequestList {
    @Valid
    @Size(min = 1)
    private List<@Valid BookingRequest> bookingRequests;
}
