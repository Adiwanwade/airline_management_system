package com.airline.airline.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
// import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @SequenceGenerator(
            name="ticket_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private String passengerAddress;

    @Column(nullable = false)
    private String passengerPhone;

    @Column(nullable = false)
    private String passengerEmail;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String seatType;

    @Column(nullable = false)
    private Boolean seatStatus;

    @Column(nullable = false, unique = true)
    private String bookingReference;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private Double price;

    
}
