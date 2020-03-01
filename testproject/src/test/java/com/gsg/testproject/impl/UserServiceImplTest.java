package com.gsg.testproject.impl;

import com.gsg.testproject.api.UserInput;
import com.gsg.testproject.api.YoutubeResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private JobServiceImpl jobService;

    @Mock
    private UserRepository userRepository;

    private UserInput userInput;

    private User user;

    @Before
    public void setUp() {
        this.userInput = UserInput
                .builder()
                .username("47ce1c6f")
                .password("test")
                .country("GE")
                .jobExecutionTime(1)
                .build();

        this.user = User
                .builder()
                .username("47ce1c6f")
                .password("test")
                .country("GE")
                .jobExecutionTime(1)
                .youtubeResults(List.of())
                .build();
    }

    @Test
    public void shouldRegisterUser() {
        doNothing().when(userRepository).save(user);
        doNothing().when(jobService).scheduleJob(userInput.getUsername(), userInput.getCountry(), userInput.getJobExecutionTime());

        userService.register(userInput);

        verify(userRepository, times(1)).save(user);
        verify(jobService, times(1)).scheduleJob(userInput.getUsername(), userInput.getCountry(), userInput.getJobExecutionTime());
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionIfInvalidJobTime() {

        userService.register(userInput.toBuilder().jobExecutionTime(0).build());
    }

    @Test(expected = Exception.class)
    public void shouldThrowException() {

        userService.register(userInput.toBuilder().country(null).build());
    }

    @Test
    public void shouldUpdateUser() {
        var country = "DE";
        doReturn(user).when(userRepository).getUser(userInput.getUsername());
        doNothing().when(userRepository).update(any());

        userService.update(userInput.toBuilder().country(country).build());

        verify(userRepository, times(1)).update(any());
        verify(jobService, times(1)).scheduleJob(userInput.getUsername(), country, userInput.getJobExecutionTime());

        assertEquals("DE", user.getCountry());
    }

    @Test
    public void shouldCheckCredentials() {
        doReturn(Optional.of(user)).when(userRepository).findUser(userInput.getUsername());

        var result = userService.checkCredentials(userInput.getUsername(), userInput.getPassword());

        assertTrue(result);
    }

    @Test
    public void shouldGetUser() {
        doReturn(user).when(userRepository).getUser(userInput.getUsername());

        var result = userService.getUser(userInput.getUsername());

        assertEquals(userInput, result);
    }

    @Test
    public void shouldNotGetJobResult() {
        doReturn(user).when(userRepository).getUser(userInput.getUsername());

        var result = userService.getJobResult(userInput.getUsername());

        assertNull(result);

    }

    @Test
    public void shouldGetJobResult() {
        var newUser = user.toBuilder().youtubeResults(List.of(new YoutubeResult("", "", LocalDateTime.now().toString(), null))).build();
        doReturn(newUser).when(userRepository).getUser(userInput.getUsername());

        var result = userService.getJobResult(userInput.getUsername());

        assertEquals(newUser.getYoutubeResults().get(0), result);
    }
}
