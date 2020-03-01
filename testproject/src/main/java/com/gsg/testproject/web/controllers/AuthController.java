package com.gsg.testproject.web.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * In the ideal world this should be done by Spring Security
 */
@RestController
@RequestMapping("")
public class AuthController {

    @PostMapping("log-out")
    public void logOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
