package com.mahmoudh.kiwe.controller;


import com.mahmoudh.kiwe.dto.*;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.security.JwtUtil;
import com.mahmoudh.kiwe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LogInCredential authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getCredential(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getCredential());

        final String token = jwtTokenUtil.generateToken(userDetails);

        User user=(User)userDetails;
        return ResponseEntity.ok(new JwtTokenResponse(user.getFirstname(),user.getLastname(),user.getUsername(),user.getEmail(), user.getAge(), token));
    }


    @PostMapping("/auth/signUp")
    public ResponseEntity<Message> createUser(@RequestBody User user) throws Exception {
        User u1=userService.getUserByEmailOrUserName(user.getUsername());
        User u2=userService.getUserByEmailOrUserName(user.getEmail());
        if(user.getFirstname()==null||user.getLastname()==null||user.getUsername()==null||
                user.getAge()==null||user.getEmail()== null||user.getPassword()==null){
            return new ResponseEntity<>(new Message("complete all requirement"), HttpStatus.BAD_REQUEST);
        }
        if(!userService.isEmail(user.getEmail())){
            return new ResponseEntity<>(new Message("not valid email"), HttpStatus.BAD_REQUEST);
        }if(u1!=null){
            return new ResponseEntity<>(new Message("Username is already used"), HttpStatus.BAD_REQUEST);
        }if(u2!=null){
            return new ResponseEntity<>(new Message("Email is already used"), HttpStatus.BAD_REQUEST);
        }if(user.getAge()<18) {
            return new ResponseEntity<>(new Message("You have to be 18 at least to sign up"), HttpStatus.BAD_REQUEST);
        }
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User u=userService.saveNewUser(user);
            return  new ResponseEntity<>(new Message("user successfully registered"), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/changePassword")
    public ResponseEntity<Message> changePassword(@RequestBody ChangePasswordRequest changeBody, Principal principal) throws Exception {
        User user=userService.getUserByEmailOrUserName(principal.getName());
        if(changeBody.getNewPassword()==null||changeBody.getOldPassword()==null){
            return new ResponseEntity<>(new Message("complete all requirement"), HttpStatus.BAD_REQUEST);
        }
        String newPassword=passwordEncoder.encode(changeBody.getNewPassword());
        if(!passwordEncoder.matches(changeBody.getOldPassword(), user.getPassword()))
            return new ResponseEntity<>(new Message("old password is wrong"), HttpStatus.BAD_REQUEST);
        if(passwordEncoder.matches(changeBody.getNewPassword(), user.getPassword()))
            return new ResponseEntity<>(new Message("New password mustn't be similar to old password"), HttpStatus.BAD_REQUEST);
        try{
            user.setPassword(newPassword);
            User u=userService.saveNewUser(user);
            return  new ResponseEntity<>(new Message("Password successfully Changed"), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/hello")
    String hello(Principal principal){
        return principal.getName();
    }
}
