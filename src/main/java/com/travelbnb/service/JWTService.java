package com.travelbnb.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.travelbnb.entity.AppUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    /* first read all the values of properties file here
     * create 3 variables -> algorithm, issuer, expirytime
     * these 3 variables should get values from properties file
     * */

    /* @Value annotation comes from the spring framework which is responsible to read the values of the key present in
     * the properties file
     * this will automatically go  to your properties file against this key whatever the value is there gets store in it
     */
    @Value("${jwt.algorithm.key}")           //this will read the value from properties file
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private int expiryTime;


    private Algorithm algorithm;

    private final String USER_NAME = "username";
    //acts as a constant and key name

    //when you start the project you want few methods to automatically runs and do some task even before you run the project
    //the moment i start the project i want the algorithm variable to be initialised with HS256.
    //i want this variable to be initialised with the algorithm
    @PostConstruct  //this method will automatically be called when project runs //comes from hibernate
    public void postConstruct() {
//        System.out.println(algorithmKey);
        algorithm = Algorithm.HMAC256(algorithmKey);//it is going to apply this algorithm key and it will initialise the
        // variable algorithm
        //only with this key this algorithm (HS256) will produce ann encrypted result
        // and only with this key this algorithm (HS256) will be decrypted
        //so this algorithm now works with the secret key . if secret key is missing this algorithm will not work
    }

    public String generateToken(AppUser user) { //supplied AppUser user b/c to generate a token i'll have to give the username
        //and for that username  the token generation will happen (in payload {user name}, {issuer}, {expirytime})
        //username will come from entity b/c this object has the username, issuer and expiryTime is present
        //the header has an algorithm and the algorithm variable is also ready
        //signature is also present here as key
        String token=JWT.create()
                .withClaim(USER_NAME, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))  //24hrs from now , current time in ms + 24hrs ms
                .withIssuer(issuer).sign(algorithm);
      //  System.out.println("Generated token: " + token);
        return JWT.create()
                .withClaim(USER_NAME, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiryTime))  //24hrs from now , current time in ms + 24hrs ms
                .withIssuer(issuer).sign(algorithm);
    }

    /************verificaation of token and getting username*************/
//    public String getUserName(String token) {
//        //verification of token
//        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
//
//        //using decodedJWT use method claim() to get username
//        //from JWT I want claim(means username).
//        return decodedJWT.getClaim(USER_NAME).asString();  //asString() converts type claim to String as methods returns
//        //String
//    }

    public String getUserName(String token) {
//        System.out.println("before get user name token:"+ token);
        DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);
        String userName = decodedJWT.getClaim(USER_NAME).asString();
      //  System.out.println("Decoded Username: " + userName); // Add this line for debugging
        return userName;
    }
}
