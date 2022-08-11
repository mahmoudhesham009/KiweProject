package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.service.UtilService;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.regex.Pattern;

@Service
public class UtilServiceImpl implements UtilService {

    @Override
    public boolean isEmail(String emailAddress) {
        return Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}")
                .matcher(emailAddress)
                .matches();
    }

    @Override
    public String generateRandomPassword() {

        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;

    }
}
