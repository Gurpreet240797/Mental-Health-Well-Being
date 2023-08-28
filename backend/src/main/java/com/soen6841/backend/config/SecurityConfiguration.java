package com.soen6841.backend.config;

import com.soen6841.backend.service.UserAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.soen6841.backend.constant.Role.*;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserAuthenticationService();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests()
                    .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                    .antMatchers("/home/**").permitAll()
                    .antMatchers("/manager/**").hasAuthority(MANAGER.toString())
                    .antMatchers("/patient/**").hasAuthority(PATIENT.toString())
                    .antMatchers("/doctor/**").hasAuthority(DOCTOR.toString())
                    .antMatchers("/counselor/**").hasAuthority(COUNSELOR.toString())
                    .and()
                .csrf()
                    .disable()
                .cors()
                    .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll();

    }
}
