package com.travelbnb.config;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
// These are the first level of classes that gets loaded into springIOC when started
public class SecurityConfig {

    //in order to run AuthorizationFiler method , create object of JWTRequestFilter
   private JWTRequestFilter jwtRequestFilter;

  //constructor based dependency injection
    public SecurityConfig(JWTRequestFilter jwtRequestFilter) {

      this.jwtRequestFilter = jwtRequestFilter;
//
//
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        //before any other filter that happens or before even you run this SecurityFiler methods you run that method first
        //String tokenHeader = request.getHeader("Authorization");  this methods run first  //giving priority to this first
        //before the JWTRequestFilter run AuthorizationFilter first
//        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);

        //saving from hacking attacks
        //cross-site-request-forgery
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        //Authorize all request
      // add country only work with token not directly so comment this part
          http.authorizeHttpRequests().anyRequest().permitAll();
//http.authorizeHttpRequests()1
        //which url canbe accessed after authorization and which url canbe accessed without authorization
        //**everything under that url is open
    //    .requestMatchers("/api/v1/countries/**").
        //but for now i will just ensure that only this url that i am now giving here this url i want to secure it this is what i am interested in
      //  .requestMatchers("/api/v1/countries/addCountry").
        //which url u want to keep that open
       // .requestMatchers("/api/v1/user/login","/api/v1/user/createUser")2
      //  .permitAll()3
       // .requestMatchers("/api/v1/countries/addCountry").hasAnyRole("ROLE_ADMIN")
      //  .requestMatchers("/api/v1/countries/addCountry").hasAnyRole("ADMIN","USER")
      //  .requestMatchers("/api/v1/countries/addCountry").hasRole("ADMIN")4
      //  .authenticated();
     //   .requestMatchers("/api/v1/photos/upload").hasAnyRole("ADMIN","USER")5
     //   .anyRequest().authenticated();6

        return http.build();
    }
}