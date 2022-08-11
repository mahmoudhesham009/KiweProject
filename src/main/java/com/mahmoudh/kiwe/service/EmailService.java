package com.mahmoudh.kiwe.service;

import com.mahmoudh.kiwe.dto.Message;

public interface EmailService {

    Message sendEmailWithNewPassword(String newPassword, String email);


}
