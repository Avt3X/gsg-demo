package com.gsg.testproject.web.security;

import com.gsg.testproject.api.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class BasicAuthenticationManager implements AuthenticationManager {

    private final UserService userService;

    BasicAuthenticationManager(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = ((String) authentication.getPrincipal());
        var password = ((String) authentication.getCredentials());

        if (!userService.checkCredentials(username, password)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
