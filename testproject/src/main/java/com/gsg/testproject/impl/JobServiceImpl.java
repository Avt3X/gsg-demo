package com.gsg.testproject.impl;

import com.gsg.testproject.api.JobService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
class JobServiceImpl implements JobService {

    /**
     * Association between user -> scheduled job
     * In the ideal world this should be persistent
     */
    private static final Map<String, ScheduledFuture<?>> USERS_JOB = new HashMap<>();
    private static final String JOB_RESULT_TOPIC = "/topic/job/result";

    private final ScheduledExecutorService executorService;
    private final YoutubeClient youtubeClient;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    JobServiceImpl(YoutubeClient youtubeClient,
                   UserRepository userRepository,
                   SimpMessagingTemplate simpMessagingTemplate) {
        this.youtubeClient = youtubeClient;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void scheduleJob(String username, String countryCode, int rate) {
        terminateJob(username);

        Runnable job = getJob(username, countryCode);

        var future = executorService.scheduleWithFixedDelay(job, rate, rate, TimeUnit.MINUTES);

        log.info("Job scheduled for {} with {} {}", username, countryCode, rate);

        USERS_JOB.put(username, future);
    }

    private Runnable getJob(String username, String countryCode) {
        return () -> {
            try {
                var result = youtubeClient.getTrendedVideoForCounty(countryCode);

                var user = userRepository.getUser(username);

                user.getYoutubeResults().add(result);

                userRepository.update(user);

                log.info("Job executed for {}", user);

                simpMessagingTemplate.convertAndSend(JOB_RESULT_TOPIC, result);
            } catch (Exception e) {
                log.error(e);
            }
        };
    }

    private void terminateJob(String user) {
        if (USERS_JOB.containsKey(user)) {
            USERS_JOB.get(user).cancel(true);
            USERS_JOB.remove(user);
        }
    }
}
