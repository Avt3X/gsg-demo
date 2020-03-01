package com.gsg.testproject.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.gsg.testproject.api.YoutubeResult;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonDeserialize(builder = User.UserBuilder.class)
class User {

    @NonNull private final String username;
    @NonNull private final String password;
    @NonNull private String country;
    @NonNull private int jobExecutionTime;

    @NonNull private final List<YoutubeResult> youtubeResults;

    @JsonPOJOBuilder(withPrefix = "")
    static class UserBuilder {}
}
