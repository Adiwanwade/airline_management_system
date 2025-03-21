package com.airline.airline.service;

import com.airline.airline.Entity.Schedule;
import com.airline.airline.Entity.Ticket;
import com.airline.airline.repository.ScheduleRepository;
import com.airline.airline.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, ScheduleRepository scheduleRepository) {
        this.ticketRepository = ticketRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<Ticket> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> ticketPage = ticketRepository.findAll(pageable);
        
        return ticketPage.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Ticket createTicket(Long scheduleId, Ticket ticket) {
        // First, check if the schedule exists
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        // Get flight capacity from the associated flight
        int flightCapacity = schedule.getFlight().getCapacity();
        int availableSeats = schedule.getAvailableSeats();

        // Check if seats are available
        if (availableSeats <= 0) {
            throw new RuntimeException("No seats available for this flight");
        }

        String bookingReference = generateBookingReference();
        String seatNo = generateSeatNumber(flightCapacity, availableSeats);
        double price = schedule.getPrice();

        Ticket newTicket = Ticket.builder()
                .schedule(schedule)
                .passengerName(ticket.getPassengerName())
                .passengerAddress(ticket.getPassengerAddress())
                .passengerPhone(ticket.getPassengerPhone())
                .passengerEmail(ticket.getPassengerEmail())
                .seatNumber(seatNo)
                .seatType(ticket.getSeatType())
                .seatStatus(true)
                .bookingReference(bookingReference)
                .bookingDate(LocalDateTime.now())
                .price(price)
                .build();

        // Update available seats in schedule
        schedule.setAvailableSeats(availableSeats - 1);
        scheduleRepository.save(schedule);

        Ticket savedTicket = ticketRepository.save(newTicket);
        return convertToDTO(savedTicket);
    }

    public Ticket getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
        return convertToDTO(ticket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findTicketsByTicketId(id)
                .orElseThrow(() -> new EntityNotFoundException("Active ticket not found with id: " + id));

        ticket.setSeatStatus(false);
        ticketRepository.delete(ticket);

        ticket.getSchedule().setAvailableSeats(ticket.getSchedule().getAvailableSeats() + 1);
    }

    private String generateBookingReference() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateSeatNumber(int flightCapacity, int flightAvailableSeats) {
        int takenSeats = flightCapacity - flightAvailableSeats;
        int row = (takenSeats / 6) + 1;
        char column = (char) ('A' + (takenSeats % 6));
        return row + String.valueOf(column);
    }

    private Ticket convertToDTO(Ticket ticket) {
        return Ticket.builder()
                .ticketId(ticket.getTicketId())
                .bookingReference(ticket.getBookingReference())
                .seatNumber(ticket.getSeatNumber())
                .price(ticket.getPrice())
                .bookingDate(ticket.getBookingDate())
                .seatStatus(ticket.getSeatStatus())
                .passengerName(ticket.getPassengerName())
                .passengerAddress(ticket.getPassengerAddress())
                .passengerPhone(ticket.getPassengerPhone())
                .passengerEmail(ticket.getPassengerEmail())
                .seatType(ticket.getSeatType())
                .build();
    }
}
