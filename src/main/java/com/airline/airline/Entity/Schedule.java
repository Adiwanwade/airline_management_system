package com.airline.airline.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "scheduleId"
)
public class Schedule {

    @Id
    @SequenceGenerator(
            name="schedule_sequence",
            sequenceName = "schedule_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "schedule_sequence"
    )
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonBackReference
    private Flight flight;

    @Column(nullable = false)
    private LocalDateTime departureDateTime;

    @Column(nullable = false)
    private LocalDateTime arrivalDateTime;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer availableSeats;

    @Transient
    public Duration getDuration() {
        return Duration.between(departureDateTime, arrivalDateTime);
    }

}