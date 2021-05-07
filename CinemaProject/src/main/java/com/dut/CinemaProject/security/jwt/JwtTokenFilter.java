package com.dut.CinemaProject.security.jwt;

import com.dut.CinemaProject.dao.domain.JwtBlacklist;
import com.dut.CinemaProject.exceptions.JwtAuthenticationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException, JwtAuthenticationException {

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);

        JwtBlacklist jwtBlacklist = jwtTokenProvider.findTokenInBlacklist(token);

        if(jwtBlacklist == null){
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(req, res);

    }
}
