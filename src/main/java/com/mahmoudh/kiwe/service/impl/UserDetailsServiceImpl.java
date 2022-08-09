package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String credential) throws UsernameNotFoundException {
        User user=userService.getUserByEmailOrUserName(credential);
        return user;
    }
}
