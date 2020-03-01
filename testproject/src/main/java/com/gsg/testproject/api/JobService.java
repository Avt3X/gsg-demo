package com.gsg.testproject.api;

public interface JobService {

    void scheduleJob(String user, String countryCode, int rate);
}
