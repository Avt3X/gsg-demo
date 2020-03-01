package com.gsg.testproject.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

// TODO better to have interface

@Component
class UserRepository {

    private static final String EXTENSION = "json";
    private static final String DELIMITER = ".";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    User getUser(String username) {
        var path = String.join(DELIMITER, username, EXTENSION);
        return getUser(Path.of(path));
    }


    Optional<User> findUser(String username) {
        try {
            return Files
                    .list(Path.of(""))
                    .filter(p -> p.toString().startsWith(username))
                    .findFirst()
                    .map(Path::toAbsolutePath)
                    .map(this::getUser);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to find user", e);
        }
    }

    void save(User user) {
        try {
            var path = String.join(DELIMITER, user.getUsername(), EXTENSION);
            if (Files.exists(Paths.get(path))) {
                throw new IllegalStateException("User already exists: " + user.getUsername());
            } else  {
                Files.createFile(Paths.get(path));
            }
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(path), user);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save user", e);
        }
    }

    void update(User user) {
        try {
            var path = String.join(DELIMITER, user.getUsername(), EXTENSION);
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new FileWriter(path), user);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to update user", e);
        }
    }

    private User getUser(Path path) {
        try {
            return OBJECT_MAPPER.readValue(new File(path.toUri()), User.class);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to get user", e);
        }
    }
}
