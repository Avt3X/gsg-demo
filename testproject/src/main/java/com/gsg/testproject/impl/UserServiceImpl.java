package com.gsg.testproject.impl;

import com.gsg.testproject.api.JobService;
import com.gsg.testproject.api.UserInput;
import com.gsg.testproject.api.UserService;
import com.gsg.testproject.api.YoutubeResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

// TODO add custom exceptions

@Component
class UserServiceImpl implements UserService {

    private final JobService jobService;
    private final UserRepository userRepository;

    UserServiceImpl(JobService jobService,
                    UserRepository userRepository) {
        this.jobService = jobService;
        this.userRepository = userRepository;
    }

    @Override
    public void register(UserInput userInput) {
        var user = buildUser(userInput);

        userRepository.save(user);
    }

    @Override
    public void update(UserInput userInput) {
        var username = userInput.getUsername();

        var user = userRepository.getUser(username);

        setIfExists(user::setCountry, userInput.getCountry());
        setIfExists(user::setJobExecutionTime, userInput.getJobExecutionTime());

        userRepository.update(user);

        jobService.scheduleJob(username, user.getCountry(), user.getJobExecutionTime());
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        var user = userRepository.findUser(username);

        return user
                .filter(u -> u.getUsername().equals(username))
                .filter(u -> u.getPassword().equals(password))
                .isPresent();
    }

    @Override
    public UserInput getUser(String username) {
        var user = userRepository.getUser(username);

        /*
          We should reschedule job because it's in memory
         */
        jobService.scheduleJob(username, user.getCountry(), user.getJobExecutionTime());

        return UserInput
                .builder()
                .country(user.getCountry())
                .jobExecutionTime(user.getJobExecutionTime())
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }

    @Override
    public YoutubeResult getJobResult(String username) {
        var user = userRepository.getUser(username);
        var results = user.getYoutubeResults();
        return results.size() == 0 ? null : results.get(results.size() - 1);
    }

    private User buildUser(UserInput userInput) {
        return User
                .builder()
                .username(getOrThrow(userInput.getUsername()))
                .password(getOrThrow(userInput.getPassword()))
                .jobExecutionTime(getJobTimeIfValid(getOrThrow(userInput.getJobExecutionTime())))
                .country(getOrThrow(userInput.getCountry()))
                .youtubeResults(List.of())
                .build();
    }

    private int getJobTimeIfValid(int time) {
        if (time > 0 && time <= 60) {
            return time;
        }
        throw new IllegalArgumentException("Invalid job execution time: " + time);
    }

    private <T> T getOrThrow(T value) {
        return Optional.ofNullable(value).orElseThrow(IllegalArgumentException::new);
    }

    private <T> void setIfExists(Consumer<T> consumer, T value) {
        Optional.ofNullable(value).ifPresent(consumer);
    }
}
