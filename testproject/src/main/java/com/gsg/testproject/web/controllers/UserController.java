package com.gsg.testproject.web.controllers;

import com.gsg.testproject.api.UserInput;
import com.gsg.testproject.api.UserService;
import com.gsg.testproject.api.YoutubeResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public void register(@RequestBody UserInput userInput) {
        try {
            userService.register(userInput);
        } catch (IllegalStateException e) {
            log.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to register user");
        } catch (IllegalArgumentException e) {
            log.error(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }
    }

    @PutMapping()
    public void update(@RequestBody UserInput userInput) {
        try {
            userService.update(userInput.toBuilder().username(getCurrentUser()).build());
        } catch (Exception e) {
            log.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to update user");
        }
    }

    @GetMapping()
    public UserInput getUser() {
        return userService.getUser(getCurrentUser());
    }

    @GetMapping("job/result")
    public YoutubeResult getLastResult() {
        return userService.getJobResult(getCurrentUser());
    }

    private String getCurrentUser() {
        return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
