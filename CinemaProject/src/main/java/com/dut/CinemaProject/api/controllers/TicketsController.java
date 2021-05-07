package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dto.Ticket.PurchaseTicket;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicketsList;
import com.dut.CinemaProject.dto.Ticket.TicketDto;
import com.dut.CinemaProject.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("tickets")
@AllArgsConstructor
public class TicketsController {
    private final ITicketService ticketService;

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id){
        ticketService.deleteTicket(id);
    }

    @GetMapping("user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TicketDto> getByUser(@PathVariable Long id){
        return ticketService.getUsersTickets(id);
    }

    @PostMapping("purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDto purchaseTicket(@RequestBody PurchaseTicket purchaseTicket){
        return ticketService.purchaseTicket(purchaseTicket);
    }

    @PostMapping("purchaselist")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TicketDto> purchaseTickets(@RequestBody PurchaseTicketsList ticketsData){
        return ticketService.purchaseTickets(ticketsData);
    }
}
