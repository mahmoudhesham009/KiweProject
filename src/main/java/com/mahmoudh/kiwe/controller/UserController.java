package com.mahmoudh.kiwe.controller;


import com.mahmoudh.kiwe.dto.*;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.security.JwtUtil;
import com.mahmoudh.kiwe.service.EmailService;
import com.mahmoudh.kiwe.service.UserService;
import com.mahmoudh.kiwe.service.UtilService;
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
    JwtUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    UtilService utilService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LogInCredential authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getCredential(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getCredential());

        final String token = jwtTokenUtil.generateToken(userDetails);

        User user = (User) userDetails;
        return ResponseEntity.ok(new JwtTokenResponse(user.getFirstname(), user.getLastname(), user.getUsername(), user.getEmail(), user.getAge(), token));
    }


    @PostMapping("/auth/signUp")
    public ResponseEntity<Message> createUser(@RequestBody User user) throws Exception {
        try {
            Message errorMessage = userService.validateUserSignUp(user);
            if (errorMessage != null) return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User u = userService.saveNewUser(user);
            return new ResponseEntity<>(new Message("user successfully registered"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("/changePassword")
    public ResponseEntity<Message> changePassword(@RequestBody ChangePasswordRequest changeBody, Principal principal) throws Exception {
        try {
            User user = userService.getUserByEmailOrUserName(principal.getName());
            Message errorMessage = userService.validateChangePassword(changeBody, user);
            if (errorMessage != null) return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            String newPassword = passwordEncoder.encode(changeBody.getNewPassword());
            user.setPassword(newPassword);
            User u = userService.saveNewUser(user);
            return new ResponseEntity<>(new Message("Password successfully Changed"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/auth/forgetPassword")
    public ResponseEntity<Message> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) throws Exception {
        User user = userService.getUserByEmailOrUserName(forgetPasswordRequest.getEmail());
        if (user == null)
            return new ResponseEntity<>(new Message("there is no user with that email"), HttpStatus.BAD_REQUEST);
        try {
            String generatedPassword = utilService.generateRandomPassword();
            String newPassword = passwordEncoder.encode(generatedPassword);
            user.setPassword(newPassword);
            User u = userService.saveNewUser(user);
            Message m = emailService.sendEmailWithNewPassword(generatedPassword, forgetPasswordRequest.getEmail());
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
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
}
