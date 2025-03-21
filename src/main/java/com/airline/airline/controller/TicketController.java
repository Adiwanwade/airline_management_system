package com.airline.airline.controller;

import com.airline.airline.Entity.Schedule;
import com.airline.airline.Entity.Ticket;
import com.airline.airline.Entity.TicketRequest;
import com.airline.airline.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Ticket> tickets = ticketService.getAllTickets(page, size);
            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketRequest request) {
        try {
            // Enhanced validation
            if (!isValidRequest(request)) {
                return ResponseEntity.badRequest().build();
            }

            // Create ticket from request
            Ticket ticket = Ticket.builder()
                .passengerName(request.getPassengerName())
                .passengerAddress(request.getPassengerAddress())
                .passengerPhone(request.getPassengerPhone())
                .passengerEmail(request.getPassengerEmail())
                .seatType(request.getSeatType())
                .seatStatus(true)
                .build();

            // Let service handle schedule association
            Ticket createdTicket = ticketService.createTicket(request.getScheduleId(), ticket);
            return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    private boolean isValidRequest(TicketRequest request) {
        return request.getScheduleId() != null &&
               request.getPassengerName() != null &&
               request.getPassengerEmail() != null &&
               request.getPassengerPhone() != null &&
               request.getSeatType() != null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}