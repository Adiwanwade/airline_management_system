package com.airline.airline.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketRequest {
    private Long scheduleId;
    private String passengerName;
    private String passengerAddress;
    private String passengerPhone;
    private String passengerEmail;
    private String seatType;
}
