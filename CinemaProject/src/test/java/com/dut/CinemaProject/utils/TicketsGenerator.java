package com.dut.CinemaProject.utils;

import com.dut.CinemaProject.dao.domain.*;
import lombok.Getter;

public class TicketsGenerator {
    @Getter
    public static class Builder {
        private Ticket newTicket;

        public Builder() {
            newTicket = new Ticket();
        }

        public Builder withId(Long id) {
            newTicket.setId(id);
            return this;
        }

        public Builder withRow(Integer row) {
            newTicket.setRow(row);
            return this;
        }

        public Builder withPlace(Integer place) {
            newTicket.setPlace(place);
            return this;
        }

        public Builder withSession(Session session) {
            newTicket.setSession(session);
            return this;
        }

        public Builder withCustomer(User customer) {
            newTicket.setCustomer(customer);
            return this;
        }

        public Ticket build() {
            return newTicket;
        }
    }
}