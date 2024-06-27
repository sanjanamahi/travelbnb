package com.travelbnb.service;


import com.travelbnb.entity.AppUser;
import com.travelbnb.payload.LoginDto;
import com.travelbnb.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private AppUserRepository appUserRepository;
    private JWTService jwtService;

    public UserService(AppUserRepository appUserRepository, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public AppUser createUser(AppUser user){
        //Bcrypt this password before saving it in database
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10)));
        AppUser createdUser = appUserRepository.save(user);
        return createdUser;
    }


//    public boolean verifyLogin( LoginDto loginDto) {
//        Optional<AppUser> opUsername = appUserRepository.findByUsername(loginDto.getUsername());
//        if (opUsername.isPresent()){
    //converting optional class to entity class
//            AppUser appUser = opUsername.get();
//            return BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword());
//        }
//        return false;
//    }


    public String verifyLogin( LoginDto loginDto) {
        Optional<AppUser> opUsername = appUserRepository.findByUsername(loginDto.getUsername());
        if (opUsername.isPresent()){
            AppUser appUser = opUsername.get();
            if(BCrypt.checkpw(loginDto.getPassword(),appUser.getPassword())){
                //calling JWTService
                String token = jwtService.generateToken(appUser);
                return token;
            }
        }
        return null;
    }
}
