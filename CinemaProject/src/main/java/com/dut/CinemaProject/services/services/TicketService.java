package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.domain.Session;
import com.dut.CinemaProject.dao.domain.Ticket;
import com.dut.CinemaProject.dao.domain.User;
import com.dut.CinemaProject.dao.repos.SessionRepository;
import com.dut.CinemaProject.dao.repos.TicketRepository;
import com.dut.CinemaProject.dao.repos.UserRepository;
import com.dut.CinemaProject.dto.Ticket.Place;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicket;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicketsList;
import com.dut.CinemaProject.dto.Ticket.TicketDto;
import com.dut.CinemaProject.exceptions.BadRequestException;
import com.dut.CinemaProject.exceptions.ItemNotFoundException;
import com.dut.CinemaProject.exceptions.ValidationException;
import com.dut.CinemaProject.services.interfaces.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;


    @Override
    public List<TicketDto> getUsersTickets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ItemNotFoundException("User not found"));
        return ticketRepository.findTicketsByCustomer(user).stream().map(TicketDto::new).collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(Long id) {
        return new TicketDto(ticketRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Ticket not found")));
    }

    @Override
    public void deleteTicket(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Ticket not found"));

        ticketRepository.delete(t);
    }

    @Override
    public TicketDto purchaseTicket(PurchaseTicket purchaseTicket) {
        Session session = sessionRepository.findById(purchaseTicket.getSessionId())
                .orElseThrow(ItemNotFoundException::new);

        if (session.getIsCanceled())
            throw new BadRequestException("Session is already canceled!");

        if (session.getDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Session is already held!");

        if(session.getHall().getRowsAmount() < purchaseTicket.getRow())
            throw new ValidationException("Row out of bounce");

        if(session.getHall().getPlaces() < purchaseTicket.getPlace())
            throw new ValidationException("Place out of bounce");

        if(ticketRepository.countTicketsByUserId(purchaseTicket.getUserId()) >= 10)
            throw new ValidationException("User can't buy tickets more than 10!");

        User user = userRepository.findById(purchaseTicket.getUserId())
                .orElseThrow(ItemNotFoundException::new);

        Optional<Ticket> existingTicket = ticketRepository.getTicketByDetails(purchaseTicket.getPlace(),
                purchaseTicket.getRow(), purchaseTicket.getSessionId());

        if(existingTicket.isPresent())
            throw new ValidationException("Requested ticket is sold out");

        Ticket ticket = new Ticket();
        ticket.setCustomer(user);
        ticket.setSession(session);
        ticket.setRow(purchaseTicket.getRow());
        ticket.setPlace(purchaseTicket.getPlace());

        return new TicketDto(ticketRepository.save(ticket));
    }

    @Override
    public List<TicketDto> purchaseTickets(PurchaseTicketsList ticketsData) {
        if (ticketsData.getSessionId() == null || ticketsData.getUserId() == null)
            throw new BadRequestException("ID values can not be null!");

        Session session = sessionRepository.findById(ticketsData.getSessionId())
                .orElseThrow(() -> new ItemNotFoundException("Session not found"));

        if (session.getIsCanceled())
            throw new BadRequestException("Session is already canceled!");

        if (session.getDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Session is already held!");

        User user = userRepository.findById(ticketsData.getUserId())
                .orElseThrow(() -> new ItemNotFoundException("User not found"));

        if (ticketsData.getPlaces() == null || ticketsData.getPlaces().size() == 0)
            throw new BadRequestException("You trying to buy 0 tickets!");

        if (ticketsData.getPlaces().size() + ticketRepository.countTicketsByUserId(user.getId()) > 10)
            throw new ValidationException("User can't buy more than 10 tickets!");

        List<Ticket> ticketsDb = new ArrayList<>();
        for (Place place: ticketsData.getPlaces()
             ) {
            if(session.getHall().getRowsAmount() < place.getRow())
                throw new ValidationException("Row out of bounce (" + place.getRow() + " of " + session.getHall().getRowsAmount() + ")");

            if(session.getHall().getPlaces() < place.getPlace())
                throw new ValidationException("Place out of bounce (" + place.getPlace() + " of " + session.getHall().getPlaces() + ")");

            Optional<Ticket> existingTicket = ticketRepository.getTicketByDetails(place.getPlace(),
                    place.getRow(), session.getId());

            if(existingTicket.isPresent())
                throw new ValidationException("Requested ticket is sold out");

            Ticket ticket = new Ticket();
            ticket.setCustomer(user);
            ticket.setPlace(place.getPlace());
            ticket.setRow(place.getRow());
            ticket.setSession(session);

            ticketsDb.add(ticket);
        }

        return ticketRepository.saveAll(ticketsDb)
                .stream()
                .map(TicketDto::new)
                .collect(Collectors.toList());
    }
}
