package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dto.Ticket.PurchaseTicket;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicketsList;
import com.dut.CinemaProject.dto.Ticket.TicketDto;

import java.util.List;

public interface ITicketService {
    List<TicketDto> getUsersTickets(Long userId);
    TicketDto purchaseTicket(PurchaseTicket purchaseTicket);
    TicketDto getTicketById(Long id);
    void deleteTicket(Long id);
    List<TicketDto> purchaseTickets(PurchaseTicketsList ticketsData);
}
