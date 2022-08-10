package com.mahmoudh.kiwe.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahmoudh.kiwe.dto.LogInCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JSONAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            BufferedReader reader=request.getReader();
            StringBuffer st=new StringBuffer();

            String line;
            while((line= reader.readLine())!=null){
                st.append(line);
            }
            LogInCredential logInCredential=objectMapper.readValue(st.toString(),LogInCredential.class);
            UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(logInCredential.getCredential(),logInCredential.getPassword());
            setDetails(request,token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
