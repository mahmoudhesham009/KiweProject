package com.mahmoudh.kiwe.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    @Autowired
    UserDetailsService userDetailsService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = getAuth(request);
        if(auth==null) {
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    UsernamePasswordAuthenticationToken getAuth(HttpServletRequest request){
        String token=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(token==null||!token.startsWith("Bearer ")) return null;
        token=token.replace("Bearer ","");

        String uesrname= JWT.require(Algorithm.HMAC256("secret")).build().verify(token).getSubject();
        if(uesrname==null)return null;
        UserDetails userDetails = userDetailsService.loadUserByUsername(uesrname);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null,
                userDetails.getAuthorities());
    }
}
