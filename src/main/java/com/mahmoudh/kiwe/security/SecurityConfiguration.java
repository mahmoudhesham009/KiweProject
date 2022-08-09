package com.mahmoudh.kiwe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    private final String secret;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfiguration(AuthSuccessHandler authSuccessHandler,@Value("secret") String secret) {
        this.authSuccessHandler = authSuccessHandler;
        this.secret = secret;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/h2-console/**","/api/user/login","/api/user/signup","/api/user/forgetPassword").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager))
                .httpBasic(Customizer.withDefaults());
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public JSONAuthenticationFilter authenticationFilter() throws Exception {
        JSONAuthenticationFilter filter = new JSONAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authSuccessHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


}
