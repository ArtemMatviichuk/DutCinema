package com.dut.CinemaProject.config;

import com.dut.CinemaProject.security.jwt.JwtConfigurer;
import com.dut.CinemaProject.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login", "/auth/register", "/auth/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/check-{email}").permitAll()
                .antMatchers("/auth/is-admin-true").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/halls").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/halls/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/halls/{id}").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/movies").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/movies/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/movies/{id}").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/reviews/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/reviews/movie/**").hasRole("USER")

                .antMatchers(HttpMethod.POST, "/sessions").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/sessions/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/sessions/{id}").hasRole("ADMIN")

                .antMatchers(HttpMethod.PUT, "/users/{id}/block").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
