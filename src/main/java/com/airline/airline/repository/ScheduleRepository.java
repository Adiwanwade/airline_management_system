package com.airline.airline.repository;

import com.airline.airline.Entity.Flight;
import com.airline.airline.Entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // Find schedules by flight
    List<Schedule> findByFlight(Flight flight);
    
    // Find schedules by flight with pagination
    Page<Schedule> findByFlight(Flight flight, Pageable pageable);
    
    // Find schedules between dates
    List<Schedule> findByDepartureDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Find available schedules (seats > 0)
    List<Schedule> findByAvailableSeatsGreaterThan(int seats);
    
    // Find schedules by price range
    List<Schedule> findByPriceBetween(double minPrice, double maxPrice);
    
    // Custom query to find schedules by flight number
    @Query("SELECT s FROM Schedule s WHERE s.flight.flightNo = :flightNo")
    List<Schedule> findByFlightNumber(@Param("flightNo") String flightNo);
    
    // Find upcoming schedules
    @Query("SELECT s FROM Schedule s WHERE s.departureDateTime > :now")
    List<Schedule> findUpcomingSchedules(@Param("now") LocalDateTime now);
    
    // Check if schedule exists
    boolean existsByFlightAndDepartureDateTime(Flight flight, LocalDateTime departureDateTime);
}
