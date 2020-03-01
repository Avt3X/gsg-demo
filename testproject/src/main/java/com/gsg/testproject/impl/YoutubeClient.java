package com.gsg.testproject.impl;

import com.gsg.testproject.api.YoutubeResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Log4j2
@Component
class YoutubeClient {

    private static final String ROOT_URL = "https://www.googleapis.com/youtube/v3";
    private static final String VIDEO_PREFIX = "https://youtube.com/watch?v=";

    private final RestTemplate restTemplate;
    private final String apiKey;

    YoutubeClient(RestTemplateBuilder restTemplateBuilder,
                  @Value("${youtube.api.key}") String apiKey) {
        this.restTemplate = restTemplateBuilder
                .rootUri(ROOT_URL)
                .build();
        this.apiKey = apiKey;
    }

    YoutubeResult getTrendedVideoForCounty(String countryCode) {
        try {
            var videoId = getTrendedVideoId(countryCode);
            var commentId = getUpVotedCommentId(videoId);

            return new YoutubeResult(buildVideoUrl(videoId), buildCommentUrl(videoId, commentId), LocalDateTime.now().toString(), null);
        } catch (Exception e) {
            log.error(e);
            return new YoutubeResult(null, null, LocalDateTime.now().toString(), e.getMessage());
        }
    }

    /**
     * In the ideal world we should create POJOs
     */
    private String getUpVotedCommentId(String videoId) {
        var url = "/commentThreads?part=snippet,replies&maxResults=1&order=relevance&videoId="+videoId+"&key="+apiKey;
        var result = restTemplate.getForObject(url, Map.class);

        return ((String) ((List<Map<String, Object>>) result.get("items")).get(0).get("id"));
    }

    private String getTrendedVideoId(String countryCode) {
        var url = "/videos?&part=snippet,contentDetails,statistics&maxResults=1&chart=mostPopular&regionCode="+countryCode+"&key="+ apiKey;
        var result = restTemplate.getForObject(url, Map.class);

        return ((String) ((List<Map<String, Object>>) result.get("items")).get(0).get("id"));
    }

    private String buildVideoUrl(String videoId) {
        return VIDEO_PREFIX + videoId;
    }

    private String buildCommentUrl(String videoId, String commentId) {
        return buildVideoUrl(videoId) + "&lc=" + commentId;
    }
}
