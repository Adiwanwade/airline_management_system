package com.airline.airline.service;

import com.airline.airline.Entity.Flight;
import com.airline.airline.Entity.Schedule;
import com.airline.airline.repository.FlightRepository;
import com.airline.airline.repository.ScheduleRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final ScheduleRepository scheduleRepository;

    public FlightService(FlightRepository flightRepository, ScheduleRepository scheduleRepository) {
        this.flightRepository = flightRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<Flight> getAllFlights(String sort) {
        Sort sortOrder;
        if (sort != null) {
            sortOrder = Sort.by(sort.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC, "flightNo");
        } else {
            sortOrder = Sort.by("flightNo");
        }

        return flightRepository.findAll(sortOrder).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Flight getFlightByFlightId(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + id));
        return convertToDTO(flight);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getFlightSchedules(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
        

        return flight.getSchedules().stream()
                .map(this::convertToScheduleDTO)
                .collect(Collectors.toList());
    }

    public Flight createFlight(Flight flight) {
        if (flight.getFlightId() != null) {
            throw new RuntimeException("New flight cannot have an ID");
        }
        Flight savedFlight = flightRepository.save(flight);
        return convertToDTO(savedFlight);
    }

    public Flight updateFlight(Flight flight) {
        if (flight.getFlightId() == null) {
            throw new RuntimeException("Flight ID cannot be null for update");
        }
        
        // Check if flight exists
        flightRepository.findById(flight.getFlightId())
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flight.getFlightId()));

        Flight updatedFlight = flightRepository.save(flight);
        return convertToDTO(updatedFlight);
    }

    public void deleteFlight(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new RuntimeException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }

    private Flight convertToDTO(Flight flight) {
        return Flight.builder()
                .flightId(flight.getFlightId())
                .flightNo(flight.getFlightNo())
                .airline(flight.getAirline())
                .origin(flight.getOrigin())
                .flightDestination(flight.getFlightDestination())
                .capacity(flight.getCapacity())
                .build();
    }

    private Schedule convertToScheduleDTO(Schedule schedule) {
        return Schedule.builder()
                .scheduleId(schedule.getScheduleId())
                .departureDateTime(schedule.getDepartureDateTime())
                .arrivalDateTime(schedule.getArrivalDateTime())
                .distance(schedule.getDistance())
                .price(schedule.getPrice())
                .availableSeats(schedule.getAvailableSeats())
                .flight(Flight.builder()
                        .flightId(schedule.getFlight().getFlightId())
                        .flightNo(schedule.getFlight().getFlightNo())
                        .build())
                .build();
    }
}
