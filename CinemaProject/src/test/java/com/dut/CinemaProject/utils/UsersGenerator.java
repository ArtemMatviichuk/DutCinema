package com.dut.CinemaProject.utils;

import com.dut.CinemaProject.dao.domain.Role;
import com.dut.CinemaProject.dao.domain.Status;
import com.dut.CinemaProject.dao.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.List;

public class UsersGenerator {
    @Getter
    public static class Builder {
        private User newPerson;
        private BCryptPasswordEncoder bCryptPasswordEncoder;

        public Builder() {
            newPerson = new User();
            bCryptPasswordEncoder = new BCryptPasswordEncoder();
        }

        public Builder withId(Long id) {
            newPerson.setId(id);
            return this;
        }

        public Builder withFirstName(String firstName) {
            newPerson.setFirstName(firstName);
            return this;
        }

        public Builder withLastName(String lastName) {
            newPerson.setLastName(lastName);
            return this;
        }

        public Builder withEmail(String email) {
            newPerson.setEmail(email);
            return this;
        }

        public Builder withPassword(String password) {
            newPerson.setPassword(password);
            return this;
        }

        public Builder withHashedPassword(String password) {
            newPerson.setPassword(bCryptPasswordEncoder.encode((password)));
            return this;
        }

        public Builder withCreated(Date created) {
            newPerson.setCreated(created);
            return this;
        }

        public Builder withUpdated(Date updated) {
            newPerson.setUpdated(updated);
            return this;
        }

        public Builder withStatus(Status status) {
            newPerson.setStatus(status);
            return this;
        }

        public Builder withRoles(List<Role> roles) {
            newPerson.setRoles(roles);
            return this;
        }

        public User build() {
            return newPerson;
        }
    }
}

