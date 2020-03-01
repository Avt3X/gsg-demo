package com.gsg.testproject.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

class CredentialsAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    CredentialsAuthenticationFilter(AuthenticationManager authenticationManager,
                                    AuthenticationSuccessHandler successHandler) {
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        Authentication authentication = retrieveAuthentication(req);
        return getAuthenticationManager().authenticate(authentication);
    }

    private Authentication retrieveAuthentication(HttpServletRequest req) {
        Credentials credentials = retrieveCredentials(req);
        return toAuthentication(credentials);
    }

    private Authentication toAuthentication(Credentials credentials) {
        return new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword(), new ArrayList<>());
    }

    private Credentials retrieveCredentials(HttpServletRequest req){
        try {
            return OBJECT_MAPPER.readValue(req.getInputStream(), Credentials.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse login request", e);
        }
    }

    @Getter
    @Setter
    public static final class Credentials {
        private String username;
        private String password;
    }
}
