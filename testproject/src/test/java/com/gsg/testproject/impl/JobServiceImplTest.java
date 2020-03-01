package com.gsg.testproject.impl;

import com.gsg.testproject.api.YoutubeResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

    @InjectMocks
    private JobServiceImpl jobService;

    private ScheduledExecutorService executorService;

    @Mock
    private YoutubeClient youtubeClient;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private User user;

    @Before
    public void setUp() {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.user = User
                .builder()
                .username("47ce1c6f")
                .password("test")
                .country("GE")
                .jobExecutionTime(1)
                .youtubeResults(List.of(new YoutubeResult("", "", "", "")))
                .build();
    }

    /**
     * In the ideal world we should wait to check if job executed
     */
    @Test
    public void shouldScheduleJob() {

        jobService.scheduleJob("47ce1c6f", "GE", 1);
    }
}
