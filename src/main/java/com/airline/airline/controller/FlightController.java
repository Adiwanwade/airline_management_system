package com.airline.airline.controller;

import com.airline.airline.Entity.Flight;
import com.airline.airline.Entity.Schedule;
import com.airline.airline.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/flights", produces = "application/json")
public class FlightController {
    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // Get all flights with optional sorting
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights(
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Flight> flights = flightService.getAllFlights(sort);
        return ResponseEntity.ok(flights);
    }

    // Get flight by ID
    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        try {
            Flight flight = flightService.getFlightByFlightId(id);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get schedules for a specific flight
    @GetMapping("/{id}/schedules")
    public ResponseEntity<List<Schedule>> getFlightSchedules(@PathVariable Long id) {
        try {
            Flight flight = flightService.getFlightByFlightId(id);
            if (flight == null) {
                return ResponseEntity.notFound().build();
            }
            
            List<Schedule> schedules = flightService.getFlightSchedules(id);
            if (schedules.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            
            return ResponseEntity.ok(schedules);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    // Create new flight
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        try {
            Flight createdFlight = flightService.createFlight(flight);
            return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update flight
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        try {
            flight.setFlightId(id);
            Flight updatedFlight = flightService.updateFlight(flight);
            return ResponseEntity.ok(updatedFlight);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete flight
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        try {
            flightService.deleteFlight(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

