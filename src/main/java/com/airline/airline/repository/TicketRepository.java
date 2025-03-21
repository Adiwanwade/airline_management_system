package com.airline.airline.repository;

import com.airline.airline.Entity.Schedule;
import com.airline.airline.Entity.Ticket;

import org.springframework.lang.NonNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Basic queries
    Optional<Ticket> findByBookingReference(String bookingReference);

    Optional<Ticket> findTicketsByTicketId(Long ticketId);
    @NonNull Page<Ticket> findAll(  @NonNull Pageable pageable);
    
    // Schedule related queries
    List<Ticket> findBySchedule(Schedule schedule);
    List<Ticket> findByScheduleAndSeatStatusTrue(Schedule schedule);
    
    // Passenger related queries
    List<Ticket> findByPassengerEmail(String email);
    List<Ticket> findByPassengerPhone(String phone);
    
    // Booking date related queries
    List<Ticket> findByBookingDateBetween(LocalDateTime start, LocalDateTime end);
    
    // Custom queries
    @Query("SELECT t FROM Ticket t WHERE t.schedule.scheduleId = :scheduleId AND t.seatStatus = true")
    List<Ticket> findActiveTicketsBySchedule(@Param("scheduleId") Long scheduleId);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.schedule.scheduleId = :scheduleId AND t.seatStatus = true")
    long countActiveTicketsBySchedule(@Param("scheduleId") Long scheduleId);
    
    // Existence checks
    boolean existsByBookingReference(String bookingReference);
    boolean existsBySeatNumberAndSchedule(String seatNumber, Schedule schedule);
}