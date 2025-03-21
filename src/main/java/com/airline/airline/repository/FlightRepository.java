package com.airline.airline.repository;

import com.airline.airline.Entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> getFlightByFlightId(Long flightId);
    @NonNull List<Flight> findAll(@NonNull Sort sort);
    @NonNull Page<Flight> findAll(@NonNull Pageable pageable);
    boolean existsByFlightNo(String flightNo);
    List<Flight> findByOriginAndFlightDestination(String origin, String destination);
    List<Flight> findByAirline(String airline);
}
