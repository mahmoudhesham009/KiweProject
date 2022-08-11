package com.mahmoudh.kiwe.service;

import com.mahmoudh.kiwe.dto.ChangePasswordRequest;
import com.mahmoudh.kiwe.dto.Message;
import com.mahmoudh.kiwe.entity.User;

public interface UserService {
    User saveNewUser(User user);
    User getUserByEmailOrUserName(String credential);
    Message validateUserSignUp(User user);
    Message validateChangePassword(ChangePasswordRequest changeBody, User user);


}
