package com.airline.airline.configuration;

import com.airline.airline.Entity.Flight;
import com.airline.airline.Entity.Schedule;
import com.airline.airline.Entity.Ticket;
import com.airline.airline.repository.FlightRepository;
import com.airline.airline.repository.ScheduleRepository;
import com.airline.airline.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class FlightConfig {
    @Bean
    CommandLineRunner runner(FlightRepository flightRepository, ScheduleRepository scheduleRepository, TicketRepository ticketRepository) {
        return _ -> {
            Flight flight1 = Flight.builder()
                    .flightNo("AI101")
                    .airline("Air India")
                    .origin("Mumbai")
                    .flightDestination("Delhi")
                    .capacity(150)
                    .build();

            Flight flight2 = Flight.builder()
                    .flightNo("IG202")
                    .airline("IndiGo")
                    .origin("Bangalore")
                    .flightDestination("Hyderabad")
                    .capacity(180)
                    .build();

            Flight flight3 = Flight.builder()
                    .flightNo("SJ303")
                    .airline("SpiceJet")
                    .origin("Chennai")
                    .flightDestination("Kolkata")
                    .capacity(120)
                    .build();

            // Save flights first
            flightRepository.saveAll(Arrays.asList(flight1, flight2, flight3));

            Schedule schedule1 = Schedule.builder()
                    .flight(flight1) // Associate with flight1
                    .departureDateTime(LocalDateTime.now().plusDays(1).withHour(8).withMinute(0))
                    .arrivalDateTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                    .distance(30000D)
                    .price(5000.0)
                    .availableSeats(150)
                    .build();

            Schedule schedule2 = Schedule.builder()
                    .flight(flight1) // Associate with flight1
                    .departureDateTime(LocalDateTime.now().plusDays(1).withHour(14).withMinute(0))
                    .arrivalDateTime(LocalDateTime.now().plusDays(1).withHour(16).withMinute(30))
                    .distance(7000D)
                    .price(5500.0)
                    .availableSeats(150)
                    .build();

            // Save schedules after flights
            scheduleRepository.saveAll(Arrays.asList(schedule1, schedule2));

            // Create default tickets
            Ticket ticket1 = Ticket.builder()
                    .schedule(schedule1)
                    .passengerName("John Doe")
                    .passengerAddress("123 Main St, Mumbai")
                    .passengerPhone("+91-9876543210")
                    .passengerEmail("john.doe@example.com")
                    .seatNumber("12A")
                    .seatType("ECONOMY")
                    .seatStatus(true)
                    .bookingReference("TKT20250319001")
                    .bookingDate(LocalDateTime.now())
                    .price(5000.0)
                    .build();

            Ticket ticket2 = Ticket.builder()
                    .schedule(schedule1)
                    .passengerName("Jane Smith")
                    .passengerAddress("456 Park Ave, Delhi")
                    .passengerPhone("+91-9876543211")
                    .passengerEmail("jane.smith@example.com")
                    .seatNumber("14B")
                    .seatType("BUSINESS")
                    .seatStatus(true)
                    .bookingReference("TKT20250319002")
                    .bookingDate(LocalDateTime.now())
                    .price(7500.0)
                    .build();

            // Save tickets after schedules
            ticketRepository.saveAll(Arrays.asList(ticket1, ticket2));

            // Update available seats for schedule1
            schedule1.setAvailableSeats(schedule1.getAvailableSeats() - 2);
            scheduleRepository.save(schedule1);
        };
    }
}
