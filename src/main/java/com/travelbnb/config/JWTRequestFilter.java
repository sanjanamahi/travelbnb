package com.travelbnb.config;

import com.travelbnb.entity.AppUser;
import com.travelbnb.repository.AppUserRepository;
import com.travelbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;
import java.util.Collections;
import java.util.Optional;


@Component   //@component does it makes a ordinary java class a springBoot class. so that object can be created
//I can also write @Service interchangeably, but here component is a good thing b/c we ae not implementing service here.
public class JWTRequestFilter extends OncePerRequestFilter {
//since this has an incomplete method doFilterInternal implement that


    private JWTService jwtService;
    private AppUserRepository appUserRepository;

    public JWTRequestFilter(JWTService jwtService, AppUserRepository appUserRepository) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //this method already has a request(this object is the one that automatically takes the incoming request
        // after you log in) , response and filterChain

        //whenever a Http request comes with the token you need to run this line first/method first
        //go to SecurityConfig
      //  if() not here after below line
      String tokenHeader = request.getHeader("Authorization");
      if(tokenHeader!=null&& tokenHeader.startsWith("Bearer")) {
         // System.out.println(tokenHeader);
          //  filterChain.doFilter(request,response);
          String token = tokenHeader.substring(8, tokenHeader.length() - 1);
        //  System.out.println(token);

          //calling jwtService for getUsername
          String userName = jwtService.getUserName(token);
          //System.out.println(userName); when add country get authenticated we comment this out
          Optional<AppUser> opUsername = appUserRepository.findByUsername(userName);
          if(opUsername.isPresent()){
              AppUser appUser = opUsername.get();
              UsernamePasswordAuthenticationToken authenticationToken =
                      new UsernamePasswordAuthenticationToken(appUser,null, Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())));
              authenticationToken.setDetails(new WebAuthenticationDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
      }
        filterChain.doFilter(request,response);


        /*String tokenHeader = request.getHeader("Authorization");
        System.out.println("Token Header: " + tokenHeader); // Add this line for debugging

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
           String token = tokenHeader.substring(8,tokenHeader.length()-1);
            System.out.println("Token: " + token); // Add this line for debugging

            // Calling jwtService for getUsername
            String userName = jwtService.getUserName(token);
            System.out.println("Username from Token: " + userName); // Add this line for debugging
        }
        filterChain.doFilter(request, response)*/;
    }
}



