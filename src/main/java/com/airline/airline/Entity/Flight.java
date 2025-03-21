// Flight Entity
package com.airline.airline.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "flightId"
)
public class Flight {
    @Id
    @SequenceGenerator(
            name="Flight_sequence",
            sequenceName = "Flight_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "Flight_sequence"
    )
    private Long flightId;

    @Column(nullable = false, unique = true)
    private String flightNo;

    @Column(nullable = false)
    private String airline;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String flightDestination;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Schedule> schedules = new HashSet<>();

    // Helper methods to manage bidirectional relationship
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setFlight(this);
    }

    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        if (schedule.getFlight() == this) {
            schedule.setFlight(null);
        }
    }
}