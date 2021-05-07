package com.dut.CinemaProject.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dut.CinemaProject.dao.domain.*;
import com.dut.CinemaProject.dao.repos.SessionRepository;
import com.dut.CinemaProject.dao.repos.TicketRepository;
import com.dut.CinemaProject.dao.repos.UserRepository;
import com.dut.CinemaProject.dto.Ticket.Place;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicket;
import com.dut.CinemaProject.dto.Ticket.PurchaseTicketsList;
import com.dut.CinemaProject.exceptions.*;
import com.dut.CinemaProject.services.services.TicketService;
import com.dut.CinemaProject.utils.TicketsGenerator;
import com.dut.CinemaProject.utils.UsersGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTests {

    @InjectMocks
    TicketService ticketService;

    @Mock
    UserRepository userRepository;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    SessionRepository sessionRepository;

    @Test
    public void getUsersTickets_shouldThrowItemNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn((Optional.empty()));

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                ticketService.getUsersTickets(anyLong())
        );

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    public void getUsersTickets_customUserExist_shouldBeCallOnce() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        ticketService.getUsersTickets(anyLong());

        verify(ticketRepository, times(1)).findTicketsByCustomer(user);
    }

    @Test
    public void getTicketById_shouldThrowItemNotFoundException() {
        when(ticketRepository.findById(anyLong())).thenReturn((Optional.empty()));

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                ticketService.getTicketById(anyLong())
        );

        assertTrue(exception.getMessage().contains("Ticket not found"));
    }

    @Test
    public void getTicketById_customTicketExist_shouldBeCallOnce() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setMovie(movie);
        session.setHall(hall);
        Ticket ticket = new TicketsGenerator.Builder()
                .withId(1L)
                .withSession(session)
                .build();
        when(ticketRepository.findById(anyLong())).thenReturn((Optional.of(ticket)));
        ticketService.getTicketById(1L);

        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    public void deleteTicket_shouldThrowItemNotFoundException() {
        when(ticketRepository.findById(anyLong())).thenReturn((Optional.empty()));

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                ticketService.getTicketById(anyLong())
        );

        assertTrue(exception.getMessage().contains("Ticket not found"));
    }

    @Test
    public void deleteTicket_customTicketExist_shouldBeCallOnce() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);

        Session session = new Session();
        session.setId(1L);
        session.setMovie(movie);
        session.setHall(hall);
        Ticket ticket = new TicketsGenerator.Builder()
                .withId(1L)
                .withSession(session)
                .build();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(anyLong());

        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test(expected = ItemNotFoundException.class)
    public void purchaseTicket_shouldThrowItemNotFoundException() {
        when(sessionRepository.findById(anyLong())).thenReturn((Optional.empty()));

        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);
        ticketService.purchaseTicket(purchaseTicket);
    }

    @Test
    public void purchaseTicket_customSessionExistAndIsCanceledParameterIsTrue_shouldThrowBadRequestException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(true);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);

        Exception exception = assertThrows(BadRequestException.class, ()->
                ticketService.purchaseTicket(purchaseTicket)
        );

        assertTrue(exception.getMessage().contains("Session is already canceled!"));
    }

    @Test
    public void purchaseTicket_customSessionExistAndHallRowAmountLessThanTicketRow_shouldValidationException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 2, 1);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTicket(purchaseTicket)
        );

        assertTrue(exception.getMessage().contains("Row out of bounce"));
    }

    @Test
    public void purchaseTicket_customSessionExistAndHallPlacesLessThanTicketPlace_shouldValidationException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 2);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTicket(purchaseTicket)
        );

        assertTrue(exception.getMessage().contains("Place out of bounce"));
    }

    @Test
    public void purchaseTicket_customSessionExistAndCountTicketsByUserLargerThanTen_shouldValidationException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        when(ticketRepository.countTicketsByUserId(1L)).thenReturn(11);
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTicket(purchaseTicket)
        );

        assertTrue(exception.getMessage().contains("User can't buy tickets more than 10!"));
    }

    @Test(expected = ItemNotFoundException.class)
    public void purchaseTicket_customSessionExistAndUserHaveNotTicket_shouldValidationException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);

        ticketService.purchaseTicket(purchaseTicket);
    }

    @Test
    public void purchaseTicket_customSessionExistAndTicketIsSoldOut_shouldValidationException() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
        .withId(1L)
        .build();

        Ticket ticket = new TicketsGenerator.Builder()
                .withId(1L)
                .withSession(session)
                .build();
        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(ticketRepository.getTicketByDetails(purchaseTicket.getPlace(),
                purchaseTicket.getRow(), purchaseTicket.getSessionId())).thenReturn(Optional.of(ticket));

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTicket(purchaseTicket)
        );

        assertTrue(exception.getMessage().contains("Requested ticket is sold out"));
    }

    @Test
    public void purchaseTicket_customSessionAndTicketExist_shouldBeCallOnce() {
        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();

        when(sessionRepository.findById(1L)).thenReturn((Optional.of(session)));
        PurchaseTicket purchaseTicket = new PurchaseTicket(1L, 1L, 1, 1);
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));
        when(ticketRepository.getTicketByDetails(purchaseTicket.getPlace(),
                purchaseTicket.getRow(), purchaseTicket.getSessionId())).thenReturn(Optional.empty());

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCustomer(user);
        ticket.setSession(session);
        ticket.setRow(purchaseTicket.getRow());
        ticket.setPlace(purchaseTicket.getPlace());
        when(ticketRepository.save(any())).thenReturn(ticket);

        ticketService.purchaseTicket(purchaseTicket);

        verify(ticketRepository, times(1)).save(any());
    }

    @Test
    public void purchaseTickets_customTicketDataHaveNullSessionAndUserId_shouldThrowBadRequestException() {
        Place place = new Place(1,1);
        List<Place> ticketsData = new ArrayList<>();
        ticketsData.add(place);

        Exception exception = assertThrows(BadRequestException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(null, null, ticketsData))
        );

        assertTrue(exception.getMessage().contains("ID values can not be null!"));
    }

    @Test
    public void purchaseTickets_sessionNotExist_shouldThrowItemNotFoundException() {
        Place place = new Place(1,1);
        List<Place> ticketsData = new ArrayList<>();
        ticketsData.add(place);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketsData))
        );

        assertTrue(exception.getMessage().contains("Session not found"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndIsCanceledParameterIsTrue_shouldThrowBadRequestException() {
        Place place = new Place(1,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(true);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        Exception exception = assertThrows(BadRequestException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("Session is already canceled!"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndUserNotExist_shouldThrowItemNotFoundException() {
        Place place = new Place(1,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndPlacesInTicketDataIsNull_shouldThrowBadRequestException() {
        List<Place> ticketData = new ArrayList<>();

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(BadRequestException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("You trying to buy 0 tickets!"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndCountOfUserTicketsMoreThanTen_shouldThrowValidationException() {
        Place place = new Place(1,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.countTicketsByUserId(anyLong())).thenReturn(10);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("User can't buy more than 10 tickets!"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndPlaceRowIsLargerThanHallsRowAmount_shouldThrowValidationException() {
        Place place = new Place(2,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.countTicketsByUserId(anyLong())).thenReturn(1);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("Row out of bounce ("
                + place.getRow() + " of " + session.getHall().getRowsAmount() + ")"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndPlacePositionIsOutOfBounce_shouldThrowValidationException() {
        Place place = new Place(1,2);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.countTicketsByUserId(anyLong())).thenReturn(1);

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("Place out of bounce ("
                + place.getPlace() + " of " + session.getHall().getPlaces() + ")"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndTicketIsSoldOut_shouldThrowValidationException() {
        Place place = new Place(1,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();

        Ticket ticket = new TicketsGenerator.Builder()
                .withId(1L)
                .withSession(session)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.countTicketsByUserId(anyLong())).thenReturn(1);
        when(ticketRepository.getTicketByDetails(place.getPlace(),
                place.getRow(), session.getId())).thenReturn(Optional.of(ticket));

        Exception exception = assertThrows(ValidationException.class, ()->
                ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData))
        );

        assertTrue(exception.getMessage().contains("Requested ticket is sold out"));
    }

    @Test
    public void purchaseTickets_customSessionExistAndCustomUserExistAndTicketExist_shouldBeCallOnce() {
        Place place = new Place(1,1);
        List<Place> ticketData = new ArrayList<>();
        ticketData.add(place);

        Movie movie = new Movie();
        movie.setId(1L);

        Hall hall = new Hall();
        hall.setId(1L);
        hall.setRowsAmount(1);
        hall.setPlaces(1);

        Session session = new Session();
        session.setIsCanceled(false);
        session.setMovie(movie);
        session.setHall(hall);

        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.countTicketsByUserId(anyLong())).thenReturn(1);
        when(ticketRepository.getTicketByDetails(place.getPlace(),
                place.getRow(), session.getId())).thenReturn(Optional.empty());

        List<Ticket> ticketsDb = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setCustomer(user);
        ticket.setSession(session);
        ticket.setRow(place.getRow());
        ticket.setPlace(place.getPlace());
        ticketsDb.add(ticket);
        when(ticketRepository.saveAll(any())).thenReturn(ticketsDb);

        ticketService.purchaseTickets(new PurchaseTicketsList(1L, 1L, ticketData));

        verify(ticketRepository, times(1)).saveAll(any());
    }
}
