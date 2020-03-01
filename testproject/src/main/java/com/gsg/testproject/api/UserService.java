package com.gsg.testproject.api;

public interface UserService {

    void register(UserInput userInput);
    void update(UserInput userInput);
    boolean checkCredentials(String username, String password);

    UserInput getUser(String username);

    YoutubeResult getJobResult(String username);
}
