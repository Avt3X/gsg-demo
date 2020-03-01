package com.gsg.testproject.web.controllers;

import com.gsg.testproject.api.UserInput;
import com.gsg.testproject.api.UserService;
import com.gsg.testproject.api.YoutubeResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserInput userInput) {
        try {
            userService.register(userInput);
        } catch (IllegalStateException e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping()
    public ResponseEntity<String> update(@RequestBody UserInput userInput) {
        try {
            userService.update(userInput.toBuilder().username(getCurrentUser()).build());
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
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
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
