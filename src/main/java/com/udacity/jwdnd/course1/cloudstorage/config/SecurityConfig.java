package com.udacity.jwdnd.course1.cloudstorage.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/signup").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home",true);


        http.logout()
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println("User " + authentication.getName() + " is logging out");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("User " + authentication.getName() + " has logged out");
                    httpServletResponse.sendRedirect("/login");
                });
    }
}
