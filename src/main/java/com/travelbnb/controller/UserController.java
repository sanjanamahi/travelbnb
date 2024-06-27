package com.travelbnb.controller;

import com.travelbnb.entity.AppUser;
import com.travelbnb.payload.JWTTokenDto;
import com.travelbnb.payload.LoginDto;
import com.travelbnb.repository.AppUserRepository;
import com.travelbnb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    //dependency injection
    private AppUserRepository appUserRepository;
    private UserService userService;

    public UserController(AppUserRepository appUserRepository, UserService userService) {
        this.appUserRepository = appUserRepository;
        this.userService = userService;
    }


//    @PostMapping
//    public ResponseEntity<AppUser> createdUser(@RequestBody AppUser user){
//        AppUser createdUser = appUserRepository.save(user);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }

    /*********signup***********/
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody AppUser user) {
        //replace Response Entity<?> when multiple value returned
        if (appUserRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Email Id exists", HttpStatus.BAD_REQUEST);
        }
        if (appUserRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username exists", HttpStatus.BAD_REQUEST);
        }

        //calling service
        AppUser createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    /**********Login**********/
    //dont write @PostMapping twice b/c it will crash b/c it results in ambiquity of methods(confusion)
    //you cannot have @PostMapping as it is on multiple methods that will result in ambiquity.
    //this url /api/v1/user , the same url is now calling 2 methods, one url how can it call 2 methods.
    //if you want to have more than one @PostMapping then differentiate that using an appropriate convention.
//    @PostMapping("/login")
//    public ResponseEntity<String> verifyLogin(@RequestBody LoginDto loginDto){
//        boolean verifiedLogin = userService.verifyLogin(loginDto);
//        if (verifiedLogin){
//            return new ResponseEntity<>("Login successful", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Invalid username/password", HttpStatus.OK);
//    }


    /************returning token after login*************/
//    @PostMapping("/login")
//    public ResponseEntity<String> verifyLogin(@RequestBody LoginDto loginDto){
//        String token = userService.verifyLogin(loginDto);
//        if (token!=null){
//            return new ResponseEntity<>(token, HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Invalid token", HttpStatus.OK);
//    }


    /********Returning token in the form of JSON object**********/
    @PostMapping("/login")
    public ResponseEntity<?> verifyLogin(@RequestBody LoginDto loginDto) {
        String token = userService.verifyLogin(loginDto);
        if (token != null) {
            JWTTokenDto jwtTokenDto = new JWTTokenDto();
            jwtTokenDto.setType("JWT token");
            jwtTokenDto.setToken(token);
            return new ResponseEntity<>(jwtTokenDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.OK);
        }
    }
}