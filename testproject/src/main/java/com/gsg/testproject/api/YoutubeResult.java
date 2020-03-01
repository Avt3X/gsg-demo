package com.gsg.testproject.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class YoutubeResult {
    private String trendedVideoLink;
    private String upVotedCommentLink;
    private String createdAt;
    private String errorMessage;
}
