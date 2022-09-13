package com.example.myCinema.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.AllArgsConstructor;


/**
 * Configuring security of the api like login and logout behavior and protected url paths.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    public void configure(HttpSecurity http) throws Exception {
        
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/test/**", "/admin/**", "/appUser/**").permitAll() // for testing
            .antMatchers("/", "/index", "/start", "/appUser/register").permitAll()
            // .antMatchers("/admin/**").hasRole("ADMIN")
            // .antMatchers("/appUser/**", "/movie/**", "/theatre/**").hasAnyRole("USER", "ADMIN")
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling()
                .accessDeniedPage("/exception/errorPage")
            .and()
            .formLogin()
                .defaultSuccessUrl("/start")
            .and()
            .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }
}