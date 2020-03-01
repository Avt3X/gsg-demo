package com.gsg.testproject.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    private static final String EXTENSION = ".json";

    @InjectMocks
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        this.user = User
                .builder()
                .username("47ce1c6f")
                .password("test")
                .country("GE")
                .jobExecutionTime(1)
                .youtubeResults(List.of())
                .build();
    }

    @After
    public void after() throws IOException {
        var path = Paths.get(user.getUsername()+EXTENSION);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    public void shouldSaveUser() {
        userRepository.save(user);

        assertTrue(Files.exists(Paths.get(user.getUsername()+EXTENSION)));
    }

    @Test
    public void shouldGetUser() {
        userRepository.save(user);

        var result = userRepository.getUser(user.getUsername());

        assertEquals(user, result);
    }

    @Test
    public void shouldFindUser() {
        userRepository.save(user);

        var result = userRepository.findUser(user.getUsername());

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldNotFindUser() {
        var result = userRepository.findUser("b27c063a13c8");

        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldUpdateUser() {
        userRepository.save(user);

        userRepository.update(user.toBuilder().country("DE").build());

        var result = userRepository.getUser(user.getUsername());

        assertEquals("DE", result.getCountry());
    }

}
