package com.mahmoudh.kiwe.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    String hello(){
        return "Hello";
    }
}
