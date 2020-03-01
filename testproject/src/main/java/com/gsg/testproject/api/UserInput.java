package com.gsg.testproject.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserInput {

    private final String username;
    private final String password;
    private final String country;
    private final int jobExecutionTime;
}
